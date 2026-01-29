-- категории
insert into category (uid, name, description, creation_date, modification_date)
values (gen_random_uuid(), 'Basic Words', 'Basic English words for beginners', NOW(), NOW()),
       (gen_random_uuid(), 'IT Vocabulary', 'Technical terms for IT specialists', NOW(), NOW()),
       (gen_random_uuid(), 'Business English', 'Business and office terminology', NOW(), NOW());

-- пользователи
insert into user_progress (uid, user_id, status, word_id, attempt_counter, user_clue_types, creation_date,
                           modification_date)
select gen_random_uuid(),
       (select id from users where username = 'john_doe'),
       'LEARNED',
       (select id from word where word = 'apple'),
       3,
       'hint,translation',
       NOW(),
       NOW()
union all
select gen_random_uuid(),
       (select id from users where username = 'john_doe'),
       'IN_PROGRESS',
       (select id from word where word = 'database'),
       1,
       'hint',
       NOW(),
       NOW()
union all
select gen_random_uuid(),
       (select id from users where username = 'alice_smith'),
       'NEW',
       (select id from word where word = 'meeting'),
       0,
       NULL,
       NOW(),
       NOW()
union all
select gen_random_uuid(),
       (select id from users where username = 'bob_wilson'),
       'LEARNED',
       (select id from word where word = 'computer'),
       5,
       'translation',
       NOW(),
       NOW();

-- слова
insert into word (uid, word, translation, clue, creation_date, modification_date)
values (gen_random_uuid(), 'apple', 'яблоко', 'fruit', NOW(), NOW()),
       (gen_random_uuid(), 'database', 'база данных', 'stores information', NOW(), NOW()),
       (gen_random_uuid(), 'meeting', 'встреча', 'business event', NOW(), NOW()),
       (gen_random_uuid(), 'computer', 'компьютер', 'electronic device', NOW(), NOW()),
       (gen_random_uuid(), 'strategy', 'стратегия', 'plan for success', NOW(), NOW()),
       (gen_random_uuid(), 'network', 'сеть', 'connected computers', NOW(), NOW());

-- вложения (примеры)
insert into attachment (uid, file_url, media_type, creation_date, modification_date)
values (gen_random_uuid(), 'https://example.com/images/apple.jpg', 'image/jpeg', NOW(), NOW()),
       (gen_random_uuid(), 'https://example.com/audio/database.mp3', 'audio/mpeg', NOW(), NOW());

-- связка слов с вложениями
insert into word_attachment (uid, word_id, attachment_id, creation_date, modification_date)
select gen_random_uuid(),
       (select id from word where word = 'apple'),
       (select id from attachment where file_url = 'https://example.com/images/apple.jpg'),
       NOW(),
       NOW()
union all
select gen_random_uuid(),
       (select id from word where word = 'database'),
       (select id from attachment where file_url = 'https://example.com/audio/database.mp3'),
       NOW(),
       NOW();
-- прогресс пользователей
insert into user_progress (uid, user_id, status, word_id, attempt_counter, user_clue_types, creation_date,
                           modification_date)
select gen_random_uuid(),
       u.id,
       up.status,
       w.id,
       up.attempt_counter,
       up.user_clue_types,
       NOW(),
       NOW()
from (values ('john_doe', 'LEARNED', 'apple', 3, 'hint,translation'),
             ('john_doe', 'IN_PROGRESS', 'database', 1, 'hint'),
             ('alice_smith', 'NEW', 'meeting', 0, NULL),
             ('bob_wilson', 'LEARNED', 'computer', 5,
              'translation')) as up(username, status, word_text, attempt_counter, user_clue_types)
         join users u on u.username = up.username
         join word w on w.word = up.word_text;