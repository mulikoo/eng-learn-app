package com.mulikoo.englearnapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "phrase",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name"}, name = "uq_phrase_name"))
@NamedEntityGraph(
        name = "Phrase.withCategory",
        attributeNodes = {@NamedAttributeNode("category")}

)
public class Phrase extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "translation", nullable = false)
    private String translation;

    @Column(name = "clue")
    private String clue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

}
