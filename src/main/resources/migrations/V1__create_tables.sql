--создание таблиц
create table word
(
    ID                BIGSERIAL PRIMARY KEY,
    uid               uuid,
    word              varchar(255),
    translation       varchar(255),
    clue              varchar(255),
    creation_date     timestamp,
    modification_date date
);

create table word_Attachment
(
    ID                BIGSERIAL PRIMARY KEY,
    uid               uuid,
    word_ID           BIGINT not null,
    attachment_ID     int    not null,
    creation_date     timestamp,
    modification_date date
);

create table attachment
(
    ID                BIGSERIAL PRIMARY KEY,
    uid               uuid,
    file_url          text,
    media_type        varchar(255),
    creation_date     timestamp,
    modification_date date
);

create table users
(
    ID                BIGSERIAL PRIMARY KEY,
    uid               uuid,
    username          varchar(255) unique,
    creation_date     timestamp,
    modification_date date
);

create table user_progress
(
    ID                BIGSERIAL PRIMARY KEY,
    uid               uuid,
    user_ID           BIGINT not null,
    status            varchar(255),
    word_ID           int    not null,
    attempt_counter   int    not null,
    user_clue_types   varchar(255),
    creation_date     timestamp,
    modification_date date
);

CREATE TABLE category
(
    ID                BIGSERIAL PRIMARY KEY,
    uid               uuid,
    name              VARCHAR(255) NOT NULL UNIQUE,
    description       TEXT,
    creation_date     timestamp,
    modification_date data
);