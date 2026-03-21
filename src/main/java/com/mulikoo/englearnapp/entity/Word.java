package com.mulikoo.englearnapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity
@Table(name = "word")
public class Word extends BaseEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "translation")
    private String translation;

    @Column(name = "clue")
    private String clue;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", unique = true, nullable = false)
    private Long categoryId;

}