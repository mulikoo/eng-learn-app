-- unique констрейнты

alter table word
    add constraint fk_word_category
        foreign key (category_id) references category (id) on delete set null;

alter table word_attachment
    add constraint fk_word_attachment_word
        foreign key (word_id) references word (id) on delete set null;

alter table user_progress
    add constraint fk_user_progress_user
        foreign key (user_id) references users (id) on delete set null;

alter table users
    add constraint fk_users_current_category
        foreign key (current_category_id) references category (id) on delete set null;

alter table user_progress
    add constraint fk_user_progress_word
        foreign key (word_id) references word (id) on delete set null;



CONSTRAINT unique_word_attachment_pair
        UNIQUE (word_ID, attachment_ID)

CONSTRAINT unique_user_word_pair
        UNIQUE (user_ID, word_ID)