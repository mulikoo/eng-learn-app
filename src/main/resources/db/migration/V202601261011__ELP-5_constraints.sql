-- категории
insert into category (uid, name, description, creation_date, modification_date)
values (gen_random_uuid(), 'Basic Words', 'Basic English words for beginners', NOW(), NOW()),
       (gen_random_uuid(), 'IT Vocabulary', 'Technical terms for IT specialists', NOW(), NOW()),
       (gen_random_uuid(), 'Business English', 'Business and office terminology', NOW(), NOW());

-- пользователи
insert into users (uid, username, current_category_id, creation_date, modification_date)
values (gen_random_uuid(), 'john_doe', 1, NOW(), NOW()),
       (gen_random_uuid(), 'alice_smith', 2, NOW(), NOW()),
       (gen_random_uuid(), 'bob_wilson', 3, NOW(), NOW());

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
values (gen_random_uuid(), 1, 1, NOW(), NOW()),
       (gen_random_uuid(), 2, 2, NOW(), NOW());

-- прогресс пользователей
insert into user_progress (uid, user_id, status, word_id, attempt_counter, user_clue_types, creation_date,
                           modification_date)
values (gen_random_uuid(), 1, 'LEARNED', 1, 3, 'hint,translation', NOW(), NOW()),
       (gen_random_uuid(), 1, 'IN_PROGRESS', 2, 1, 'hint', NOW(), NOW()),
       (gen_random_uuid(), 2, 'NEW', 3, 0, NULL, NOW(), NOW()),
       (gen_random_uuid(), 3, 'LEARNED', 4, 5, 'translation', NOW(), NOW());