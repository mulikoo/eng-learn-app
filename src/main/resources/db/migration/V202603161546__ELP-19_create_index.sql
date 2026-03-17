create index if not exists idx_users_creation_data on users (creation_date);
create index if not exists idx_category_creation_date on category (creation_date);
create index if not exists idx_word_creation_data on word (creation_date);