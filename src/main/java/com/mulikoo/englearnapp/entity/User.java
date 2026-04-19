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
@NamedEntityGraph(name = "User.withRoleAndPermissions", attributeNodes = {
        @NamedAttributeNode(value = "role", subgraph = "role-subgraph")
},subgraphs = {
        @NamedSubgraph(name = "role-subgraph", attributeNodes = {
                @NamedAttributeNode(value = "permissions")
        })
})
@Table(name = "users")
public class User extends BaseEntity {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_category_id", nullable = false)
    private Category currentCategory;

    @Column(name = "password")
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;
}