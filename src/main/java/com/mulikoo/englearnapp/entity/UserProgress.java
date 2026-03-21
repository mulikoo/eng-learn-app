package com.mulikoo.englearnapp.entity;

import com.mulikoo.englearnapp.enums.ClueType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "user_progress")
public class UserProgress extends BaseEntity {

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Long userId;

    @Column(name = "status", nullable = false)
    private String status;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "word_id", unique = true ,nullable = false)
    private Long wordId;

    @Column(name = "attempt_counter", nullable = false)
    private int attemptCounter;

    @Column(name = "user_clue_types", nullable = false)
    private Set<ClueType> userClueTypes;

}
