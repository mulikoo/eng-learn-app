--создание таблиц
create table word
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid   NOT NULL UNIQUE,
    word              varchar(255),
    translation       varchar(255),
    clue              varchar(255),
    category_id       BIGINT not null,
    creation_date     timestamp,
    modification_date timestamp
);

create table word_attachment
(
    id            BIGSERIAL PRIMARY KEY,
    word_id       BIGINT not null,
    attachment_id BIGINT not null,
);

create table attachment
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid NOT NULL UNIQUE,
    file_url          text not null,
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
    add constraint uq_word_attachment_word_id_attachment_id
        unique (word_id, attachment_id);

alter table user_progress
    add constraint uq_user_progress_user_id_word_id
        unique (user_id, word_id);

alter table word_attachment
drop
constraint if exists fk_word_attachment_word,
    add constraint fk_word_attachment_word
        foreign key (word_id)
        references word (id)
        on delete
cascade;

alter table word_attachment
drop
constraint if exists fk_word_attachment_attachment,
    add constraint fk_word_attachment_attachment
        foreign key (attachment_id)
        references attachment (id)
         on delete
cascade;

alter table user_progress
drop
constraint if exists fk_user_progress_user,
    add constraint fk_user_progress_user
        foreign key (user_id)
        references users (id)
         on delete
cascade;

alter table user_progress
drop
constraint if exists fk_user_progress_word,
    add constraint fk_user_progress_word
        foreign key (word_id)
        references word (id)
         on delete
cascade;

alter table users
drop
constraint if exists fk_users_current_category,
    add constraint fk_users_current_category
        foreign key (current_category_id)
        references category (id)
         on delete
cascade;

-- unique констрайнты
create index uq_word_attachment_word_id on word_attachment (word_id);
create index uq_word_attachment_attachment_id on word_attachment (attachment_id);
create index uq_user_progress_user_id on user_progress (user_id);
create index uq_user_progress_word_id on user_progress (word_id);
create index uq_users_current_category_id on users (current_category_id);

-- индексы
create index idx_word_word on word (word);
create index idx_word_translation on word (translation);
create index idx_category_name on category (name);
create index idx_users_username on users (username);
create index idx_attachment_file_url on attachment (file_url);
create index idx_attachment_media_type on attachment (media_type);
create index idx_word_category_id_user_current_category on word(category_id, id);