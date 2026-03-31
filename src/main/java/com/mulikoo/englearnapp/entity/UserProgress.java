package com.mulikoo.englearnapp.entity;

import com.mulikoo.englearnapp.enums.ClueType;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_progress")
public class UserProgress extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserProgressStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", nullable = false)
    private Word word;

    @Column(name = "attempt_counter", nullable = false)
    private int attemptCounter;

    @Column(name = "user_clue_types")
    private Set<ClueType> userClueTypes;

}
