package com.mulikoo.englearnapp.entity;

import com.mulikoo.englearnapp.enums.ClueType;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Getter
@Setter
@Entity
@DynamicUpdate
@Table(name = "user_progress")
@NamedEntityGraph(name = "UserProgress.withWord", attributeNodes = {
        @NamedAttributeNode(value = "word", subgraph = "word-subgraph")
},subgraphs = {
        @NamedSubgraph(name = "word-subgraph", attributeNodes = {
                @NamedAttributeNode(value = "category")
        })
})
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

    @Column(name = "user_clue_types", columnDefinition = "TEXT")
    private Set<ClueType> userClueTypes;

}
