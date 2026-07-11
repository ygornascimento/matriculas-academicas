# Decisões de banco de dados — Matrículas Acadêmicas

Este documento registra as principais decisões de modelagem do banco de dados do projeto Matrículas Acadêmicas.

O objetivo é manter rastreabilidade entre o domínio, as regras de negócio e as constraints criadas na migration inicial com Flyway.

## Visão geral do modelo

O schema inicial foi modelado com as seguintes tabelas:

- `alunos`
- `cursos`
- `disciplinas`
- `curso_disciplinas`
- `turmas`
- `matriculas`

A modelagem busca atender aos fluxos principais do desafio:

- cadastro de alunos;
- cadastro de cursos;
- cadastro de disciplinas;
- associação de disciplinas a cursos;
- cadastro de turmas;
- matrícula de alunos em turmas;
- confirmação e cancelamento de matrículas;
- consulta de matrículas por aluno e por turma.

## Decisão sobre Curso, Disciplina e CursoDisciplina

Durante a modelagem, foi avaliada a possibilidade de uma disciplina pertencer diretamente a um curso.

Essa alternativa foi descartada porque acoplaria uma disciplina a um único curso, o que não representa bem o domínio acadêmico. Uma disciplina como "Banco de Dados" pode fazer parte de diferentes cursos.

Por isso, a disciplina foi modelada como uma unidade curricular reutilizável.

A associação entre curso e disciplina foi representada pela tabela `curso_disciplinas`.

Assim:

- `cursos` representa formações acadêmicas;
- `disciplinas` representa unidades curriculares reutilizáveis;
- `curso_disciplinas` representa que uma disciplina faz parte de um curso;
- `turmas` representa a oferta de uma disciplina dentro de um curso;
- `matriculas` representa o vínculo entre aluno e turma.

## Ordem das tabelas na migration

A ordem de criação das tabelas respeita as dependências entre elas:

1. `alunos`
2. `cursos`
3. `disciplinas`
4. `curso_disciplinas`
5. `turmas`
6. `matriculas`

A tabela `curso_disciplinas` depende de `cursos` e `disciplinas`.

A tabela `turmas` depende de `curso_disciplinas`.

A tabela `matriculas` depende de `alunos` e `turmas`.

## Datas e horários

Os campos técnicos de data e hora usam o tipo `timestamptz` no PostgreSQL.

Exemplo:

```sql
criado_em timestamptz not null default current_timestamp
```

Essa decisão foi tomada para representar instantes de tempo de forma mais segura, reduzindo ambiguidade de fuso horário.

No Java, esses campos são mapeados como `Instant`.

Exemplos:

```java
private Instant criadoEm;
private Instant atualizadoEm;
```

Campos como `criado_em`, `atualizado_em`, `confirmada_em` e `cancelada_em` representam eventos técnicos do sistema, não datas acadêmicas de calendário.

## Tabela alunos

A tabela `alunos` representa pessoas que podem ser matriculadas em turmas.

Campos principais:

- `id`
- `nome`
- `email`
- `criado_em`
- `atualizado_em`

Constraint principal:

```sql
constraint uk_alunos_email unique (email)
```

Essa constraint impede o cadastro de dois alunos com o mesmo e-mail.

Essa regra também poderá ser tratada na camada de service para retornar mensagens mais claras na API, mas o banco funciona como proteção final da consistência.

## Tabela cursos

A tabela `cursos` representa formações acadêmicas.

Campos principais:

- `id`
- `nome`
- `descricao`
- `criado_em`
- `atualizado_em`

Constraint principal:

```sql
constraint uk_cursos_nome unique (nome)
```

Essa constraint impede o cadastro de dois cursos com o mesmo nome.

## Tabela disciplinas

A tabela `disciplinas` representa unidades curriculares reutilizáveis.

Campos principais:

- `id`
- `nome`
- `carga_horaria`
- `criado_em`
- `atualizado_em`

Constraints principais:

```sql
constraint ck_disciplinas_carga_horaria_positiva
    check (carga_horaria > 0)
```

Essa constraint impede disciplinas com carga horária menor ou igual a zero.

```sql
constraint uk_disciplinas_nome
    unique (nome)
```

Essa constraint impede duas disciplinas com o mesmo nome no catálogo.

Nesta versão, o nome da disciplina é tratado como único globalmente para manter o cadastro simples. Em uma evolução futura, seria possível permitir nomes iguais com códigos diferentes ou estruturas mais complexas.

## Tabela curso_disciplinas

A tabela `curso_disciplinas` representa a associação entre curso e disciplina.

Campos principais:

- `id`
- `curso_id`
- `disciplina_id`
- `criado_em`

Constraints principais:

```sql
constraint fk_curso_disciplinas_curso
    foreign key (curso_id)
    references cursos (id)
```

Garante que a associação sempre aponte para um curso existente.

```sql
constraint fk_curso_disciplinas_disciplina
    foreign key (disciplina_id)
    references disciplinas (id)
```

Garante que a associação sempre aponte para uma disciplina existente.

```sql
constraint uk_curso_disciplinas_curso_disciplina
    unique (curso_id, disciplina_id)
```

Impede associar a mesma disciplina duas vezes ao mesmo curso.

Essa regra também é validada na camada de service antes de salvar a associação, permitindo uma mensagem mais clara para a API.

## Tabela turmas

A tabela `turmas` representa a oferta de uma disciplina dentro de um curso.

A turma não aponta diretamente para `disciplinas`. Ela aponta para `curso_disciplinas`.

Isso permite saber não apenas qual disciplina está sendo ofertada, mas também dentro de qual curso ela está sendo ofertada.

Campos principais:

- `id`
- `curso_disciplina_id`
- `codigo`
- `periodo`
- `limite_vagas`
- `vagas_ocupadas`
- `status`
- `criado_em`
- `atualizado_em`

Constraint de relacionamento:

```sql
constraint fk_turmas_curso_disciplina
    foreign key (curso_disciplina_id)
    references curso_disciplinas (id)
```

Garante que toda turma esteja associada a uma disciplina dentro de um curso.

Constraint de limite de vagas:

```sql
constraint ck_turmas_limite_vagas_positivo
    check (limite_vagas > 0)
```

Impede turmas com limite de vagas menor ou igual a zero.

Constraint de vagas ocupadas não negativas:

```sql
constraint ck_turmas_vagas_ocupadas_nao_negativa
    check (vagas_ocupadas >= 0)
```

Impede que uma turma tenha número negativo de vagas ocupadas.

Constraint de vagas ocupadas dentro do limite:

```sql
constraint ck_turmas_vagas_ocupadas_dentro_limite
    check (vagas_ocupadas <= limite_vagas)
```

Impede que o número de vagas ocupadas ultrapasse o limite de vagas da turma.

Constraint de status:

```sql
constraint ck_turmas_status_valido
    check (status in ('ABERTA', 'FECHADA'))
```

Garante que a turma só aceite os status previstos pelo domínio.

Constraint de código único:

```sql
constraint uk_turmas_codigo
    unique (codigo)
```

Impede duas turmas com o mesmo código.

## Tabela matriculas

A tabela `matriculas` representa o vínculo entre um aluno e uma turma.

Campos principais:

- `id`
- `aluno_id`
- `turma_id`
- `status`
- `criado_em`
- `confirmada_em`
- `cancelada_em`

Constraints de relacionamento:

```sql
constraint fk_matriculas_aluno
    foreign key (aluno_id)
    references alunos (id)
```

Garante que toda matrícula esteja associada a um aluno existente.

```sql
constraint fk_matriculas_turma
    foreign key (turma_id)
    references turmas (id)
```

Garante que toda matrícula esteja associada a uma turma existente.

Constraint de status:

```sql
constraint ck_matriculas_status_valido
    check (status in ('PENDENTE', 'CONFIRMADA', 'CANCELADA'))
```

Garante que a matrícula só aceite os status previstos pelo domínio.

Constraint de confirmação:

```sql
constraint ck_matriculas_confirmada_em
    check (
        (status = 'CONFIRMADA' and confirmada_em is not null)
        or status <> 'CONFIRMADA'
    )
```

Garante que uma matrícula confirmada tenha data de confirmação preenchida.

Constraint de cancelamento:

```sql
constraint ck_matriculas_cancelada_em
    check (
        (status = 'CANCELADA' and cancelada_em is not null)
        or status <> 'CANCELADA'
    )
```

Garante que uma matrícula cancelada tenha data de cancelamento preenchida.

Constraint de duplicidade:

```sql
constraint uk_matriculas_aluno_turma
    unique (aluno_id, turma_id)
```

Implementa a regra de que um aluno não pode se matricular duas vezes na mesma turma.

Nesta versão, mesmo que uma matrícula seja cancelada, ela permanece como histórico e não é criada uma nova matrícula para o mesmo aluno na mesma turma.

Em uma evolução futura, seria possível permitir rematrícula considerando apenas matrículas ativas como bloqueio. Essa decisão exigiria outro tipo de regra, como índice parcial no PostgreSQL ou validação baseada em status.

## Regras protegidas pelo banco

O banco protege as seguintes regras estruturais:

- aluno deve ter e-mail único;
- curso deve ter nome único;
- disciplina deve ter nome único;
- disciplina deve ter carga horária positiva;
- curso e disciplina não podem ser associados repetidamente;
- turma deve estar vinculada a uma associação entre curso e disciplina;
- turma deve ter limite de vagas positivo;
- turma não pode ter vagas ocupadas negativas;
- turma não pode ter vagas ocupadas acima do limite;
- turma só pode ter status ABERTA ou FECHADA;
- matrícula deve estar vinculada a aluno e turma existentes;
- matrícula só pode ter status PENDENTE, CONFIRMADA ou CANCELADA;
- matrícula confirmada deve ter data de confirmação;
- matrícula cancelada deve ter data de cancelamento;
- aluno não pode ter duas matrículas para a mesma turma.

## Banco e camada de service

As constraints do banco não substituem as regras na camada de service.

A estratégia adotada é:

- o service valida regras de negócio e fornece mensagens mais claras;
- o banco protege a consistência final dos dados;
- as entidades protegem pequenos estados internos, como confirmação, cancelamento, consumo e liberação de vaga.

Exemplo de divisão:

```text
Service:
- verifica se aluno existe;
- verifica se turma existe;
- verifica se turma está aberta;
- verifica se aluno já possui matrícula;
- coordena confirmação e cancelamento.

Entidade:
- matrícula sabe confirmar e cancelar seu próprio status;
- turma sabe consumir e liberar vaga.

Banco:
- impede status inválido;
- impede duplicidade;
- impede vagas ocupadas acima do limite;
- garante chaves estrangeiras.
```

## Decisão sobre migrations

Durante o desenvolvimento local, a migration `V1__create_initial_schema.sql` foi ajustada antes da entrega final para refletir melhor o domínio.

Como o banco era local e descartável, o volume do PostgreSQL pôde ser removido com:

```bash
docker compose down -v
```

Em um ambiente compartilhado ou em produção, uma migration já aplicada não deveria ser alterada. Nesse caso, seria criada uma nova migration, como `V2__...sql`.

## Limitações conhecidas

A modelagem atual não implementa:

- matriz curricular completa;
- versionamento de grade curricular;
- pré-requisitos de disciplinas;
- professor;
- sala;
- horário;
- notas;
- frequência;
- autenticação;
- rematrícula após cancelamento;
- controle avançado de concorrência para confirmação simultânea da última vaga.

Esses elementos foram deixados fora do escopo inicial para manter o foco nos requisitos centrais do desafio.