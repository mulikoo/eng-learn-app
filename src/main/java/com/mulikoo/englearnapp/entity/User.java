package com.mulikoo.englearnapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@NamedEntityGraph(name = "User.userAndCurrentCategory", attributeNodes = {
        @NamedAttributeNode("currentCategory")
})
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_category_id", nullable = false)
    private Category currentCategory;
}