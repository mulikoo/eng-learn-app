package com.mulikoo.englearnapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("users")
public class Users extends BaseEntity{

    @Column("username")
    private String username;

    @Column("current_category_id")
    private Long currentCategoryId;

}
