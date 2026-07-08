create table alunos (
    id bigserial primary key,
    nome varchar(150) not null,
    email varchar(150) not null,
    criado_em timestamptz not null default current_timestamp,
    atualizado_em timestamptz not null default current_timestamp,

    constraint uk_alunos_email unique (email)
);

create table cursos (
    id bigserial primary key,
    nome varchar(150) not null,
    descricao varchar(500),
    criado_em timestamptz not null default current_timestamp,
    atualizado_em timestamptz not null default current_timestamp,

    constraint uk_cursos_nome unique (nome)
);

create table disciplinas (
     id bigserial primary key,
     curso_id bigint not null,
     nome varchar(150) not null,
     carga_horaria integer not null,
     criado_em timestamptz not null default current_timestamp,
     atualizado_em timestamptz not null default current_timestamp,

     constraint fk_disciplinas_curso
         foreign key (curso_id)
             references cursos (id),

     constraint ck_disciplinas_carga_horaria_positiva
         --protege o banco contra carga horária inválida.
         check (carga_horaria > 0),

     constraint uk_disciplinas_curso_nome
         unique (curso_id, nome)
);

create table turmas (
    id bigserial primary key,
    disciplina_id bigint not null,
    codigo varchar(50) not null,
    periodo varchar(30) not null,
    limite_vagas integer not null,
    vagas_ocupadas integer not null default 0,
    status varchar(20) not null,
    criado_em timestamptz not null default current_timestamp,
    atualizado_em timestamptz not null default current_timestamp,

    constraint fk_turmas_disciplina
        foreign key (disciplina_id)
            references disciplinas (id),

    constraint ck_turmas_limite_vagas_positivo
        check (limite_vagas > 0),

    constraint ck_turmas_vagas_ocupadas_nao_negativa
        check (vagas_ocupadas >= 0),

    constraint ck_turmas_vagas_ocupadas_dentro_limite
        check (vagas_ocupadas <= limite_vagas),

    constraint ck_turmas_status_valido
        check (status in ('ABERTA', 'FECHADA')),

    constraint uk_turmas_codigo
        unique (codigo)
);

create table matriculas (
    id bigserial primary key,
    aluno_id bigint not null,
    turma_id bigint not null,
    status varchar(20) not null,
    criado_em timestamptz not null default current_timestamp,
    confirmada_em timestamptz,
    cancelada_em timestamptz,

    constraint fk_matriculas_aluno
        foreign key (aluno_id)
            references alunos (id),

    constraint fk_matriculas_turma
        foreign key (turma_id)
            references turmas (id),

    constraint ck_matriculas_status_valido
        check (status in ('PENDENTE', 'CONFIRMADA', 'CANCELADA')),

    constraint ck_matriculas_confirmada_em
        check (
            (status = 'CONFIRMADA' and confirmada_em is not null)
                or status <> 'CONFIRMADA'
            ),

    constraint ck_matriculas_cancelada_em
        check (
            (status = 'CANCELADA' and cancelada_em is not null)
                or status <> 'CANCELADA'
            ),

    constraint uk_matriculas_aluno_turma
        unique (aluno_id, turma_id)
);
