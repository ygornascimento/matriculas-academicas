### Documentação da API

Com o backend em execução, a documentação Swagger pode ser acessada em:

```text
http://localhost:8080/swagger-ui.html
``

# Matrículas Acadêmicas

Sistema simples para gestão de matrículas acadêmicas, desenvolvido como desafio técnico júnior full stack.

O objetivo principal é implementar os fluxos básicos de cadastro acadêmico e controle de matrículas, com foco em API REST, persistência em banco relacional, organização simples do código e regras de negócio claras.

## Escopo do desafio

A aplicação permite:

- cadastrar alunos;
- cadastrar cursos;
- cadastrar disciplinas;
- associar disciplinas a cursos;
- cadastrar turmas;
- criar matrículas de alunos em turmas;
- confirmar matrículas;
- cancelar matrículas;
- consultar matrículas por aluno;
- consultar matrículas por turma.

## Tecnologias previstas para a entrega

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Bean Validation
- PostgreSQL
- Flyway
- Docker Compose
- Angular

## Status atual do desenvolvimento

O projeto está em desenvolvimento.

Até o momento, foram implementados:

- configuração inicial do backend Spring Boot;
- configuração do PostgreSQL via Docker Compose;
- versionamento inicial do banco com Flyway;
- entidades JPA principais;
- repositories Spring Data JPA;
- services iniciais de cadastro e regras de domínio;
- documentação inicial das decisões de domínio e banco de dados.

Ainda serão implementados:

- DTOs de entrada e saída;
- controllers REST;
- validações de entrada com Bean Validation;
- tratamento global de erros;
- frontend Angular;
- documentação dos endpoints;
- testes e validação manual dos fluxos principais.

## Decisões de domínio

### Entidades principais

O domínio foi modelado com as seguintes entidades principais:

- Aluno
- Curso
- Disciplina
- CursoDisciplina
- Turma
- Matricula

Essas entidades foram escolhidas a partir do enunciado do desafio e refinadas durante a modelagem do domínio.

A entidade `CursoDisciplina` foi adicionada para representar a associação entre um curso e uma disciplina. Essa decisão permite que disciplinas existam como unidades curriculares reutilizáveis, podendo fazer parte de diferentes cursos.

### Linguagem do domínio

Nesta versão, os principais conceitos do domínio são interpretados da seguinte forma:

- `Aluno`: pessoa que pode ser matriculada em uma turma.
- `Curso`: formação acadêmica, composta por disciplinas.
- `Disciplina`: unidade curricular reutilizável, como "Banco de Dados" ou "Programação Orientada a Objetos".
- `CursoDisciplina`: associação entre um curso e uma disciplina.
- `Turma`: oferta de uma disciplina dentro de um curso, em determinado período, com limite de vagas e status.
- `Matricula`: vínculo entre um aluno e uma turma.

### Relacionamentos

Os relacionamentos principais são:

- um curso pode estar associado a várias disciplinas;
- uma disciplina pode estar associada a vários cursos;
- a associação entre curso e disciplina é representada por `CursoDisciplina`;
- uma turma representa a oferta de uma disciplina dentro de um curso;
- uma turma pertence a uma associação `CursoDisciplina`;
- um aluno pode possuir matrículas;
- uma matrícula pertence a um aluno e a uma turma.

Apesar de curso e disciplina formarem uma relação muitos-para-muitos, essa relação foi modelada com uma entidade associativa explícita (`CursoDisciplina`), em vez de um `@ManyToMany` direto.

Essa decisão torna o domínio mais claro e permite explicar melhor que uma turma não é apenas a oferta genérica de uma disciplina, mas a oferta de uma disciplina dentro de um curso específico.

### Modelagem dos relacionamentos no JPA

Nesta versão, os relacionamentos foram modelados de forma unidirecional nos pontos necessários para os casos de uso:

- `CursoDisciplina` conhece `Curso`;
- `CursoDisciplina` conhece `Disciplina`;
- `Turma` conhece `CursoDisciplina`;
- `Matricula` conhece `Aluno`;
- `Matricula` conhece `Turma`.

As entidades pai não mantêm listas dos objetos filhos neste momento. Essa decisão reduz complexidade de serialização, evita carregamento desnecessário de dados e mantém o modelo mais simples para os fluxos exigidos pelo desafio.

Consultas como matrículas por aluno e matrículas por turma serão feitas diretamente pelos repositories.

### Decisão sobre Curso, Disciplina e Turma

Durante a modelagem, foi avaliada a possibilidade de uma disciplina pertencer diretamente a um curso. Essa alternativa foi descartada porque acoplaria a disciplina a um único curso, o que não representa bem o domínio acadêmico.

Nesta versão, `Disciplina` representa uma unidade curricular reutilizável.

`Curso` representa uma formação acadêmica composta por disciplinas.

`CursoDisciplina` representa a associação entre um curso e uma disciplina.

`Turma` representa a oferta de uma disciplina dentro de um curso em determinado período, com limite de vagas, vagas ocupadas e status.

Essa modelagem evita uma simplificação excessiva do domínio sem implementar uma matriz curricular completa, que ficaria fora do escopo inicial do desafio.

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


## Regras de negócio da aplicação

As principais regras de negócio são:

- um aluno só pode ser matriculado em turmas abertas;
- uma turma possui limite de vagas;
- um aluno não pode se matricular duas vezes na mesma turma;
- uma matrícula possui status PENDENTE, CONFIRMADA ou CANCELADA;
- ao confirmar uma matrícula, a vaga da turma é consumida;
- ao cancelar uma matrícula confirmada, a vaga é liberada;
- deve haver consulta de matrículas por aluno e por turma.

Além dessas regras principais, a aplicação também considera:

- uma disciplina pode ser associada a diferentes cursos;
- uma mesma disciplina não deve ser associada duas vezes ao mesmo curso;
- uma turma deve estar vinculada a uma associação entre curso e disciplina;
- o limite de vagas de uma turma não pode ser menor que a quantidade de vagas já ocupadas.

## Decisões técnicas

### Java 17

Foi utilizado Java 17 por ser uma versão LTS amplamente adotada no ecossistema Spring Boot moderno, mantendo compatibilidade e facilidade de execução local.

### application.properties

A configuração da aplicação foi mantida em `application.properties` por simplicidade e clareza, já que o projeto possui poucas configurações iniciais.

### Flyway

O banco de dados é versionado com Flyway. Essa decisão evita depender de geração automática de tabelas pelo Hibernate e torna a estrutura do banco mais explícita.

O Hibernate é usado com validação do schema, não como responsável por criar ou alterar automaticamente as tabelas.

### PostgreSQL e constraints

O banco utiliza constraints para proteger regras estruturais importantes, como:

- e-mail único para aluno;
- nome único para curso;
- nome único para disciplina;
- associação única entre curso e disciplina;
- código único para turma;
- matrícula única por aluno e turma;
- status válidos para turma e matrícula;
- limite de vagas positivo;
- vagas ocupadas não negativas;
- vagas ocupadas menores ou iguais ao limite de vagas.

Essas constraints funcionam como uma camada final de proteção da consistência dos dados. As regras também são tratadas na camada de service quando necessário, para permitir mensagens mais claras na API.

### Datas e horários

Os campos técnicos de data e hora são armazenados no PostgreSQL usando `timestamptz` e mapeados no Java como `Instant`.

A intenção é registrar instantes de tempo de forma consistente, evitando ambiguidade de fuso horário no banco de dados.

### Sem Lombok

Optei por não utilizar Lombok neste desafio.

Embora seja uma biblioteca comum em projetos Java/Spring, preferi manter construtores, getters e métodos de domínio explícitos para deixar o fluxo do código mais transparente durante a avaliação técnica.

Como o domínio é pequeno, o custo de escrever esse código adicional é baixo, e a clareza foi priorizada.

### Sem autenticação

Esta versão não implementa autenticação nem controle de usuários administrativos.

O sistema assume uso administrativo direto, pois o foco do desafio está nos cadastros acadêmicos, no fluxo de matrícula, nas regras de negócio e na persistência dos dados.

### Organização em camadas

O backend segue uma organização simples por camadas:

- `entity`: entidades JPA e enums do domínio;
- `repository`: interfaces Spring Data JPA para persistência;
- `service`: regras de negócio e orquestração dos casos de uso;
- `exception`: exceções específicas da aplicação;
- `controller`: endpoints REST, a serem implementados;
- `dto`: objetos de entrada e saída da API, a serem implementados.

A escolha por uma organização em camadas foi feita para manter o projeto claro, fácil de navegar e compatível com o escopo júnior do desafio.

## Como rodar o projeto

> Esta seção será atualizada conforme o backend e o frontend forem concluídos.

### Pré-requisitos

- Java 17
- Maven Wrapper incluído no projeto
- Docker
- Docker Compose

### Subir o banco de dados

Na raiz do projeto:

```bash
docker compose up -d
```

### Rodar o backend

Na pasta `backend`:

```bash
./mvnw spring-boot:run
```

### Parar os containers

Na raiz do projeto:

```bash
docker compose down
```

### Apagar os dados locais do banco

Na raiz do projeto:

```bash
docker compose down -v
```

O parâmetro `-v` remove também o volume do PostgreSQL. Isso apaga os dados locais e permite recriar o banco do zero.

## Documentação complementar

- [Decisões de banco de dados](docs/database-decisions.md)

## Uso de IA no desenvolvimento

Durante o desenvolvimento deste desafio, utilizei ChatGPT como ferramenta de apoio técnico e tutoria.

O uso ocorreu principalmente nas seguintes atividades:

- organização inicial do domínio acadêmico a partir do enunciado;
- escrita e revisão dos casos de uso principais;
- discussão sobre regras de negócio de matrícula;
- revisão da separação de responsabilidades entre entidades, repositories e services;
- discussão sobre modelagem de curso, disciplina, turma e matrícula;
- apoio na elaboração do README e dos cenários de validação manual;
- revisão de decisões técnicas como Flyway, Java 17, ausência de Lombok e uso de constraints no banco.

As decisões finais de modelagem, implementação, nomes de classes, endpoints, regras de negócio e organização do código foram revisadas manualmente por mim.

A IA foi utilizada como apoio para raciocínio, revisão e organização, não como substituição da compreensão do código.

## Limitações conhecidas

- A aplicação não implementa autenticação.
- A aplicação não implementa controle avançado de concorrência para confirmações simultâneas de matrícula na última vaga.
- A aplicação não implementa rematrícula após cancelamento.
- A aplicação não implementa matriz curricular completa.
- A aplicação não implementa versionamento de grade curricular.
- A aplicação não implementa pré-requisitos de disciplinas.
- A aplicação não implementa professores, salas, horários, notas ou frequência.

Esses pontos foram deixados fora do escopo inicial para manter o foco nos fluxos principais exigidos pelo desafio: cadastros acadêmicos, turmas, matrículas, persistência e regras de negócio básicas.