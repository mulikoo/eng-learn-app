-- категории
INSERT INTO Category (name, description)
VALUES ('Основные', 'Основные слова для начинающих'),
       ('Бизнес', 'Бизнес лексика'),
       ('Путешествия', 'Слова для путешественников');

--слова
INSERT INTO Word (word, translation, clue, category_id)
SELECT 'Hello', 'Привет', 'Приветствие', ID
FROM Category
WHERE name = 'Основные';

INSERT INTO Word (word, translation, clue, category_id)
SELECT 'Goodbye', 'До свидания', 'Прощание', ID
FROM Category
WHERE name = 'Основные';

INSERT INTO Word (word, translation, clue, category_id)
SELECT 'Database', 'База данных', 'Хранилище информации', ID
FROM Category
WHERE name = 'IT';

INSERT INTO Word (word, translation, clue, category_id)
SELECT 'Airport', 'Аэропорт', 'Место для самолетов', ID
FROM Category
WHERE name = 'Путешествия';

-- типо пользователи
INSERT INTO Users (username, current_category_id)
VALUES ('john_doe', 1),
       ('jane_smith', 3);