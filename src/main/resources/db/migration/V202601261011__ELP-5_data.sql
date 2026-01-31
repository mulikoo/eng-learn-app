-- категории
insert into category (uid, name, description, creation_date, modification_date)
values (gen_random_uuid(), 'Базовый английский', 'Начальный уровень для новичков', NOW(), NOW()),
       (gen_random_uuid(), 'IT лексика', 'Техническая лексика для IT специалистов', NOW(), NOW()),
       (gen_random_uuid(), 'Бизнес английский', 'Бизнес и офис термины', NOW(), NOW());

-- пользователи
insert into users (uid, username, current_category_id, creation_date, modification_date)
values (gen_random_uuid(), 'john_doe', (select id from category where name = 'IT лексика'), NOW(), NOW()),
       (gen_random_uuid(), 'alice_smith', (select id from category where name = 'Бизнес английский'), NOW(), NOW()),
       (gen_random_uuid(), 'bob_wilson', (select id from category where name = 'Базовый английский'), NOW(), NOW());

-- слова
insert into word (uid, name, translation, clue, creation_date, modification_date)
values (gen_random_uuid(), 'apple', 'яблоко', 'фрукт', NOW(), NOW()),
       (gen_random_uuid(), 'database', 'база данных', 'хранения данных', NOW(), NOW()),
       (gen_random_uuid(), 'meeting', 'встреча', 'бизнес разговоры', NOW(), NOW()),
       (gen_random_uuid(), 'computer', 'компьютер', 'девайс для работы в IT', NOW(), NOW()),
       (gen_random_uuid(), 'strategy', 'стратегия', 'планировка события', NOW(), NOW()),
       (gen_random_uuid(), 'network', 'сеть', 'объединение компьютеров', NOW(), NOW());

-- вложения (примеры)
insert into attachment (uid, file_url, media_type, creation_date, modification_date)
values (gen_random_uuid(), 'https://example.com/images/apple.jpg', 'image/jpeg', NOW(), NOW()),
       (gen_random_uuid(), 'https://example.com/audio/database.mp3', 'audio/mpeg', NOW(), NOW());

-- связка слов с вложениями
insert into word_attachment (uid, word_id, attachment_id, creation_date, modification_date)
values (gen_random_uuid(), (select id from word where name = 'apple' and translation = 'яблоко'), (select id from attachment where file_url = 'https://example.com/images/apple.jpg'), NOW(), NOW()),
       (gen_random_uuid(), (select id from word where name = 'computer' and translation = 'компьютер'), (select id from attachment where file_url = 'https://example.com/audio/database.mp3'), NOW(), NOW());

-- прогресс пользователей
insert into user_progress (uid, user_id, status, word_id, attempt_counter, user_clue_types, creation_date, modification_date)
values (gen_random_uuid(), (select id from users where username = 'john_doe'), 'LEARNED', (select id from word where name = 'apple' and translation = 'яблоко'), 3, 'hint,translation', NOW(), NOW()),
       (gen_random_uuid(), (select id from users where username = 'john_doe'), 'IN_PROGRESS', (select id from word where name = 'database' and translation = 'база данных'), 1, 'hint', NOW(), NOW()),
       (gen_random_uuid(), (select id from users where username = 'alice_smith'), 'NEW', (select id from word where name = 'computer' and translation = 'компьютер'), 0, NULL, NOW(), NOW()),
       (gen_random_uuid(), (select id from users where username = 'bob_wilson'), 'LEARNED', (select id from word where name = 'network' and translation = 'сеть'), 5, 'translation', NOW(), NOW());