package com.mulikoo.englearnapp.entity;

import lombok.*;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;


@Getter
@Setter
@Table(name = "category")
public class Category extends BaseEntity {

    @Column("name")
    private String name;

    @Column("description")
    private String description;

}
