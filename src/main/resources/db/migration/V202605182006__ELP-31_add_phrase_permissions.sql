INSERT INTO sec.permissions (code, description, creation_date, modification_date)
VALUES
    ('PHRASE_READ', 'Чтение фраз', now(), now()),
    ('PHRASE_CREATE', 'Создание фраз', now(), now()),
    ('PHRASE_UPDATE', 'Обновление фраз', now(), now()),
    ('PHRASE_DELETE', 'Удаление фраз', now(), now()),
    ('PHRASE_LEARN', 'Изучение фраз', now(), now());

-- полномочия для роли MANAGER
INSERT INTO sec.role_permission(role_id, permission_id)
VALUES
    ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'PHRASE_READ')),
    ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'PHRASE_CREATE')),
    ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'PHRASE_UPDATE')),
    ((select id from sec.roles where code = 'MANAGER'), (select id from sec.permissions where code = 'PHRASE_DELETE'));

-- полномочия для роли User
INSERT INTO sec.role_permission(role_id, permission_id)
VALUES
    ((select id from sec.roles where code = 'USER'), (select id from sec.permissions where code = 'PHRASE_READ')),
    ((select id from sec.roles where code = 'USER'), (select id from sec.permissions where code = 'PHRASE_LEARN'));