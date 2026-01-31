package com.mulikoo.englearnapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("user_progress")
public class UserProgress extends BaseEntity {

    @Column("user_id")
    private Long userId;

    @Column("status")
    private String status;

    @Column("word_id")
    private Long wordId;

    @Column("attempt_counter")
    private int attemptCounter;

    @Column("user_clue_types")
    private String userClueTypes;

}
