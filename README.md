# Matrículas Acadêmicas

Sistema simples para gestão de matrículas acadêmicas, desenvolvido como desafio técnico júnior full stack.

O objetivo principal é implementar os fluxos básicos de cadastro acadêmico e controle de matrículas, com foco em API REST, persistência em banco relacional, organização simples do código e regras de negócio claras.

## Escopo do desafio

A aplicação permite:

- cadastrar alunos;
- cadastrar cursos;
- cadastrar disciplinas;
- cadastrar turmas;
- criar matrículas de alunos em turmas;
- confirmar matrículas;
- cancelar matrículas;
- consultar matrículas por aluno;
- consultar matrículas por turma.

## Tecnologias utilizadas

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Flyway
- Docker Compose
- Angular

## Decisões de domínio

### Entidades principais

O domínio foi modelado com as seguintes entidades principais:

- Aluno
- Curso
- Disciplina
- Turma
- Matricula

Essas entidades foram escolhidas a partir do enunciado do desafio e representam o núcleo mínimo necessário para atender aos fluxos de matrícula acadêmica.

### Relacionamentos

Os relacionamentos principais são:

- um curso possui várias disciplinas;
- uma disciplina pertence a um curso;
- uma disciplina pode ter várias turmas;
- uma turma pertence a uma disciplina;
- um aluno pode possuir matrículas;
- uma matrícula pertence a um aluno e a uma turma.

Apesar de aluno e turma formarem uma relação muitos-para-muitos, a matrícula foi modelada como entidade própria porque possui estado e regras de negócio, como pendência, confirmação, cancelamento e controle de vaga.

### Modelagem dos relacionamentos no JPA

Nesta versão, os relacionamentos foram modelados de forma unidirecional nos pontos necessários para os casos de uso:

- Disciplina conhece Curso;
- Turma conhece Disciplina;
- Matricula conhece Aluno;
- Matricula conhece Turma.

As entidades pai não mantêm listas dos objetos filhos neste momento. Essa decisão reduz complexidade de serialização, evita carregamento desnecessário de dados e mantém o modelo mais simples para os fluxos exigidos pelo desafio.

Consultas como matrículas por aluno e matrículas por turma serão feitas diretamente pelos repositories.

### Status da turma

Uma turma pode possuir os seguintes status:

- ABERTA
- FECHADA

Somente turmas abertas podem receber matrículas.

### Status da matrícula

Uma matrícula pode possuir os seguintes status:

- PENDENTE
- CONFIRMADA
- CANCELADA

Uma matrícula recém-criada inicia com status PENDENTE.

### Consumo e liberação de vagas

A vaga da turma não é consumida no momento da criação da matrícula pendente.

A vaga é consumida somente quando a matrícula é confirmada.

Ao cancelar uma matrícula confirmada, a vaga anteriormente ocupada é liberada.

### Duplicidade de matrícula

A regra "um aluno não pode se matricular duas vezes na mesma turma" foi implementada de forma direta: o sistema permite apenas uma matrícula por aluno e turma.

Mesmo que uma matrícula seja cancelada, ela permanece registrada como histórico, e o sistema não cria uma nova matrícula para o mesmo aluno na mesma turma.

Essa decisão mantém a regra simples, aderente ao enunciado e fácil de validar. Em uma evolução futura, seria possível permitir rematrícula considerando apenas matrículas ativas como bloqueio, mas essa variação não faz parte do escopo inicial.

## Regras de negócio implementadas

- Um aluno só pode ser matriculado em turmas abertas.
- Uma turma possui limite de vagas.
- Um aluno não pode se matricular duas vezes na mesma turma.
- Uma matrícula possui status PENDENTE, CONFIRMADA ou CANCELADA.
- Ao confirmar uma matrícula, a vaga da turma é consumida.
- Ao cancelar uma matrícula confirmada, a vaga é liberada.
- Deve haver consulta de matrículas por aluno e por turma.

## Decisões técnicas

### Java 17

Foi utilizado Java 17 por ser uma versão LTS amplamente adotada no ecossistema Spring Boot moderno, mantendo compatibilidade e facilidade de execução local.

### application.properties

A configuração da aplicação foi mantida em `application.properties` por simplicidade e clareza, já que o projeto possui poucas configurações iniciais.

### Flyway

O banco de dados será versionado com Flyway. Essa decisão evita depender de geração automática de tabelas pelo Hibernate e torna a estrutura do banco mais explícita.

O Hibernate será usado com validação do schema, não como responsável por criar ou alterar automaticamente as tabelas.

### Sem Lombok

Optei por não utilizar Lombok neste desafio.

Embora seja uma biblioteca comum em projetos Java/Spring, preferi manter construtores, getters e métodos de domínio explícitos para deixar o fluxo do código mais transparente durante a avaliação técnica.

Como o domínio é pequeno, o custo de escrever esse código adicional é baixo, e a clareza foi priorizada.

### Sem autenticação

Esta versão não implementa autenticação nem controle de usuários administrativos.

O sistema assume uso administrativo direto, pois o foco do desafio está nos cadastros acadêmicos, no fluxo de matrícula, nas regras de negócio e na persistência dos dados.

## Como rodar o projeto

> Esta seção será atualizada conforme a infraestrutura do projeto for implementada.

### Subir o banco de dados

```bash
docker compose up -d