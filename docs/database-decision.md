# Decisões de Banco de Dados

Projeto: Matrículas Acadêmicas

Este documento registra as principais decisões da migration inicial do banco de dados. A intenção é manter rastreabilidade entre o enunciado do desafio, as regras de negócio e as constraints aplicadas no PostgreSQL.

## 1. Tabelas da migration inicial

A migration `V1__create_initial_schema.sql` cria as tabelas mínimas necessárias para o domínio:

- `alunos`
- `cursos`
- `disciplinas`
- `turmas`
- `matriculas`

Relacionamentos:

- `disciplinas.curso_id` referencia `cursos.id`
- `turmas.disciplina_id` referencia `disciplinas.id`
- `matriculas.aluno_id` referencia `alunos.id`
- `matriculas.turma_id` referencia `turmas.id`

## 2. Datas e horários

Os campos técnicos de data e hora usam `timestamptz` no PostgreSQL. A decisão evita gravar horários sem contexto de fuso e deixa o registro mais adequado para representar instantes reais no tempo.

No Java, a recomendação é mapear esses campos como `Instant`.

```properties
spring.jpa.properties.hibernate.jdbc.time_zone=UTC
```

Campos envolvidos:

- `criado_em`
- `atualizado_em`
- `confirmada_em`
- `cancelada_em`

## 3. Constraints por tabela

### 3.1 alunos

| Constraint | Objetivo | Justificativa |
|---|---|---|
| `primary key` em `id` | Identificar unicamente cada aluno. | Necessário para relacionar aluno com matrícula. |
| `uk_alunos_email unique(email)` | Impedir dois alunos com o mesmo e-mail. | Regra simples de consistência cadastral. |
| `nome not null` | Exigir nome do aluno. | Aluno sem nome não é útil para os fluxos do sistema. |
| `email not null` | Exigir e-mail do aluno. | O e-mail funciona como dado mínimo de contato/identificação. |

### 3.2 cursos

| Constraint | Objetivo | Justificativa |
|---|---|---|
| `primary key` em `id` | Identificar unicamente cada curso. | Necessário para relacionar curso com disciplinas. |
| `uk_cursos_nome unique(nome)` | Evitar duplicidade de cursos com o mesmo nome. | Mantém cadastro básico mais consistente. |
| `nome not null` | Exigir nome do curso. | Curso sem nome não atende ao fluxo de cadastro. |

### 3.3 disciplinas

| Constraint | Objetivo | Justificativa |
|---|---|---|
| `fk_disciplinas_curso` | Garantir que toda disciplina pertença a um curso existente. | Sustenta o relacionamento Curso 1:N Disciplina. |
| `ck_disciplinas_carga_horaria_positiva` | Impedir carga horária menor ou igual a zero. | Carga horária inválida deixaria o cadastro incoerente. |
| `uk_disciplinas_curso_nome unique(curso_id, nome)` | Impedir duas disciplinas com o mesmo nome dentro do mesmo curso. | Permite nomes iguais em cursos diferentes, mas evita duplicidade dentro do curso. |
| `curso_id not null` | Exigir vínculo com curso. | No modelo escolhido, disciplina sem curso está fora do escopo. |

### 3.4 turmas

| Constraint | Objetivo | Justificativa |
|---|---|---|
| `fk_turmas_disciplina` | Garantir que toda turma pertença a uma disciplina existente. | Sustenta o relacionamento Disciplina 1:N Turma. |
| `ck_turmas_limite_vagas_positivo` | Impedir turma com limite de vagas zero ou negativo. | O desafio exige controle de limite de vagas. |
| `ck_turmas_vagas_ocupadas_nao_negativa` | Impedir quantidade negativa de vagas ocupadas. | Evita estado impossível no controle de vagas. |
| `ck_turmas_vagas_ocupadas_dentro_limite` | Impedir vagas ocupadas maiores que o limite. | Protege a regra de capacidade da turma no banco. |
| `ck_turmas_status_valido` | Permitir apenas `ABERTA` ou `FECHADA`. | O desafio exige que aluno só seja matriculado em turmas abertas. |
| `uk_turmas_codigo unique(codigo)` | Evitar duas turmas com o mesmo código. | Facilita identificação da turma na API, frontend e testes manuais. |

### 3.5 matriculas

| Constraint | Objetivo | Justificativa |
|---|---|---|
| `fk_matriculas_aluno` | Garantir que a matrícula pertença a um aluno existente. | Sustenta consulta de matrículas por aluno. |
| `fk_matriculas_turma` | Garantir que a matrícula pertença a uma turma existente. | Sustenta consulta de matrículas por turma e controle de vagas. |
| `ck_matriculas_status_valido` | Permitir apenas `PENDENTE`, `CONFIRMADA` ou `CANCELADA`. | Esses são os status definidos no enunciado do desafio. |
| `ck_matriculas_confirmada_em` | Exigir `confirmada_em` quando status for `CONFIRMADA`. | Evita matrícula confirmada sem registro temporal da confirmação. |
| `ck_matriculas_cancelada_em` | Exigir `cancelada_em` quando status for `CANCELADA`. | Evita matrícula cancelada sem registro temporal do cancelamento. |
| `uk_matriculas_aluno_turma unique(aluno_id, turma_id)` | Impedir que um aluno tenha duas matrículas na mesma turma. | Implementa diretamente a regra mínima do desafio. Mesmo matrícula cancelada permanece como histórico e bloqueia nova matrícula no escopo inicial. |

## 4. Regra de duplicidade e rematrícula

A regra foi implementada de forma direta: um aluno pode ter apenas uma matrícula por turma. Mesmo que a matrícula seja cancelada, ela permanece registrada como histórico e não é criada uma nova matrícula para o mesmo aluno na mesma turma.

Essa decisão prioriza aderência ao enunciado, simplicidade e facilidade de validação.

Em uma evolução futura, seria possível permitir rematrícula considerando apenas matrículas ativas como bloqueio, substituindo a constraint única simples por uma regra baseada em status, como um índice parcial no PostgreSQL.

## 5. O que fica fora da migration inicial

- Usuário administrativo e autenticação.
- Professor, sala, horário, grade curricular, notas e frequência.
- Controle avançado de concorrência para a última vaga.
- Rematrícula após cancelamento.
- Triggers para atualização automática de `atualizado_em`. Essa responsabilidade ficará inicialmente na aplicação.

## 6. Observação para entrevista técnica

As constraints do banco não substituem as regras de negócio no service. Elas funcionam como uma segunda camada de proteção. A aplicação deve validar as regras com mensagens compreensíveis; o banco protege a integridade caso alguma operação inconsistente tente passar.