-- индексы
CREATE INDEX idx_word_category_id ON word(category_id);
CREATE INDEX idx_user_current_category ON users(current_category_id);
CREATE INDEX idx_user_progress_user_word ON yser_progress(user_ID, word_ID);
CREATE INDEX idx_user_progress_status ON user_progress(status);
CREATE INDEX idx_category_name ON category(name);