package com.mulikoo.englearnapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@Table(name = "word")
public class Word extends BaseEntity {

    @Column("name")
    private String name;

    @Column("translation")
    private String translation;

    @Column("clue")
    private String clue;

    @Column("category_id")
    private Long categoryId;

}