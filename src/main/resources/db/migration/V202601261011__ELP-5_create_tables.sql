--создание таблиц
create table word
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid NOT NULL UNIQUE,
    word              varchar(255),
    translation       varchar(255),
    clue              varchar(255),
    creation_date     timestamp,
    modification_date timestamp
);

create table word_attachment
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid   NOT NULL UNIQUE,
    word_id           BIGINT not null,
    attachment_id     int    not null,
    creation_date     timestamp,
    modification_date timestamp
);

create table attachment
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid NOT NULL UNIQUE,
    file_url          text,
    media_type        varchar(255),
    creation_date     timestamp,
    modification_date timestamp
);

create table users
(
    id                  BIGSERIAL PRIMARY KEY,
    uid                 uuid   NOT NULL UNIQUE,
    username            varchar(255) unique,
    current_category_id BIGINT not null,
    creation_date       timestamp,
    modification_date   timestamp
);

create table user_progress
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid   NOT NULL UNIQUE,
    user_id           BIGINT not null,
    status            varchar(255),
    word_id           BIGINT not null,
    attempt_counter   int    not null,
    user_clue_types   varchar(255),
    creation_date     timestamp,
    modification_date timestamp
);

CREATE TABLE category
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid         NOT NULL UNIQUE,
    name              VARCHAR(255) NOT NULL UNIQUE,
    description       TEXT,
    creation_date     timestamp,
    modification_date timestamp
);

-- Связи
alter table word_attachment
    add constraint T fk_word_attachment_word
        foreign key (word_id) references word (id);

alter table word_attachment
    add constraint fk_word_attachment_attachment
        foreign key (attachment_id) references attachment (id);

alter table user_progress
    add constraint fk_user_progress_user
        foreign key (user_id) references users (id);

alter table user_progress
    add constraint fk_user_progress_word
        foreign key (word_id) references word (id);

alter table users
    add constraint fk_users_current_category
        foreign keyY (current_category_id) references category (id);

-- индексы
create index idx_word_attachment_word_id on word_attachment (word_id);
create index idx_word_attachment_attachment_id on word_attachment (attachment_id);
create index idx_user_progress_user_id on user_progress (user_id);
create index idx_user_progress_word_id on user_progress (word_id);
create index idx_users_current_category_id on users (current_category_id);