INSERT INTO sec.permissions (code, description, creation_date, modification_date)
VALUES ('USER_CATEGORY_CHANGE', 'Смена категории у пользователя', now(), now());

INSERT INTO sec.role_permission(role_id, permission_id)
VALUES ((select id from sec.roles where code = 'USER'),
        (select id from sec.permissions where code = 'USER_CATEGORY_CHANGE'));