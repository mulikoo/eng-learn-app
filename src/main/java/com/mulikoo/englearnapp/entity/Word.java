package com.mulikoo.englearnapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;


@Getter
@Setter
@Entity
@ToString(exclude = {"category", "attachments"})
@Table(name = "word",
        uniqueConstraints = @UniqueConstraint(columnNames = {"name", "translation"}, name = "uq_word_name_translation"))
public class Word extends BaseEntity {

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "translation", nullable = false)
    private String translation;

    @Column(name = "clue")
    private String clue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "word_attachment",
            joinColumns = @JoinColumn(name = "word_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "attachment_id", referencedColumnName = "id")
    )
    private List<Attachment> attachments;

}