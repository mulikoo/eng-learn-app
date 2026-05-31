create table if not exists phrase
(
    id                BIGSERIAL PRIMARY KEY,
    uid               uuid   NOT NULL UNIQUE,
    name              varchar(255),
    translation       varchar(255),
    clue              varchar(255),
    category_id       BIGINT not null,
    creation_date     timestamp,
    modification_date timestamp
);

ALTER TABLE phrase
    drop constraint if exists fk_phrase_category,
    ADD CONSTRAINT fk_phrase_category
        FOREIGN KEY (category_id) REFERENCES Category (id) ON DELETE SET NULL;

alter table phrase
    drop constraint if exists uq_phrase_name,
    add constraint uq_phrase_name
        unique (name);

ALTER TABLE user_progress
    ADD COLUMN if not exists phrase_id BIGINT;

ALTER TABLE user_progress
    drop constraint if exists fk_user_progress_phrase,
    ADD CONSTRAINT fk_user_progress_phrase
        FOREIGN KEY (phrase_id) REFERENCES phrase (id) ON DELETE CASCADE;

ALTER TABLE user_progress
    ALTER COLUMN word_id DROP NOT NULL;