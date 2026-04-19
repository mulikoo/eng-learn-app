CREATE SCHEMA IF NOT EXISTS sec;

DROP TABLE IF exists sec.roles;
CREATE TABLE sec.roles
(
    id                BIGSERIAL PRIMARY KEY,
    code              VARCHAR(255) NOT NULL UNIQUE,
    description       TEXT         NOT NULL,
    creation_date     timestamp,
    modification_date timestamp
);

DROP TABLE IF exists sec.permissions;
CREATE TABLE sec.permissions
(
    id                BIGSERIAL PRIMARY KEY,
    code              VARCHAR(255) NOT NULL UNIQUE,
    description       TEXT         NOT NULL,
    creation_date     timestamp,
    modification_date timestamp
);

DROP TABLE IF exists sec.role_permission;
CREATE TABLE sec.role_permission
(
    role_id       BIGINT NOT NULL,
    permission_id BIGINT NOT NULL
);

ALTER TABLE users
    ADD COLUMN role_id BIGINT;
ALTER TABLE users
    ALTER COLUMN current_category_id DROP NOT NULL;

alter table sec.role_permission
    drop constraint if exists uq_role_permission,
    add constraint uq_role_permission
        unique (role_id, permission_id);

ALTER TABLE sec.role_permission
    drop constraint if exists fk_role_permission_role,
    ADD CONSTRAINT fk_role_permission_role
        FOREIGN KEY (role_id) REFERENCES sec.roles (id) ON DELETE CASCADE;

ALTER TABLE sec.role_permission
    drop constraint if exists fk_role_permission_permission,
    ADD CONSTRAINT fk_role_permission_permission
        FOREIGN KEY (permission_id) REFERENCES sec.permissions (id) ON DELETE CASCADE;

ALTER TABLE users
    drop constraint if exists fk_user_role,
    ADD CONSTRAINT fk_user_role
        FOREIGN KEY (role_id) REFERENCES sec.roles (id) ON DELETE SET NULL;

create index idx_role_permission_role on sec.role_permission (role_id);
create index idx_role_permission_permission on sec.role_permission (permission_id);
create index idx_users_role on users (role_id);
create index idx_roles_code on sec.roles (code);
create index idx_permissions_code on sec.permissions (code);


INSERT INTO sec.roles (code, description, creation_date, modification_date)
VALUES ('ADMIN', 'Админ имеет все доступные функции', now(), now()),
       ('USER', 'Юзер с базовым набором функций', now(), now()),
       ('MANAGER', 'Mенеджер', now(), now());

INSERT INTO sec.permissions (code, description, creation_date, modification_date)
VALUES ('CATEGORY_READ', 'Чтение категорий', now(), now()),
       ('CATEGORY_CREATE', 'Создание категорий', now(), now()),
       ('CATEGORY_UPDATE', 'Обновление категорий', now(), now()),
       ('CATEGORY_DELETE', 'Удаление категорий', now(), now()),
       ('WORD_READ', 'Чтение слов', now(), now()),
       ('WORD_CREATE', 'Создание слов', now(), now()),
       ('WORD_UPDATE', 'Обновление слов', now(), now()),
       ('WORD_DELETE', 'Удаление слов', now(), now()),
       ('WORD_LEARN', 'Изучение слов', now(), now()),
       ('USER_READ', 'Чтение пользователя', now(), now()),
       ('USER_CREATE', 'Создание пользователя', now(), now()),
       ('USER_UPDATE', 'Обновление пользователя', now(), now()),
       ('USER_DELETE', 'Удаление пользователя', now(), now());

-- полномочия для роли User
INSERT INTO sec.role_permission(role_id, permission_id)
VALUES ((select id from sec.roles where code = 'USER'), (select id from sec.permissions where code = 'CATEGORY_READ')),
       ((select id from sec.roles where code = 'USER'), (select id from sec.permissions where code = 'WORD_READ')),
       ((select id from sec.roles where code = 'USER'), (select id from sec.permissions where code = 'WORD_LEARN'));

-- полномочия для роли MANAGER
INSERT INTO sec.role_permission(role_id, permission_id)
VALUES ((select id from sec.roles where code = 'MANAGER'),
        (select id from sec.permissions where code = 'CATEGORY_READ')),
       ((select id from sec.roles where code = 'MANAGER'),
        (select id from sec.permissions where code = 'CATEGORY_CREATE')),
       ((select id from sec.roles where code = 'MANAGER'),
        (select id from sec.permissions where code = 'CATEGORY_UPDATE')),
       ((select id from sec.roles where code = 'MANAGER'),
        (select id from sec.permissions where code = 'CATEGORY_DELETE')),
       ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'WORD_READ')),
       ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'WORD_CREATE')),
       ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'WORD_UPDATE')),
       ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'WORD_DELETE'));

-- полномочия для роли ADMIN
INSERT INTO sec.role_permission(role_id, permission_id)
SELECT (select id from sec.roles where code = 'ADMIN'), id
FROM sec.permissions;

UPDATE users
set role_id = (select id from sec.roles where code = 'USER')
where users.role_id is null;

ALTER TABLE users
    ALTER COLUMN role_id DROP NOT NULL;

ALTER TABLE users
    ALTER COLUMN role_id SET NOT NULL;