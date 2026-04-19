package com.mulikoo.englearnapp.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter
@Table(name = "roles", schema = "sec")
public class Role extends CoreEntity{

    @Column(name = "code")
    private String code;

    @Column(name = "description")
    private String description;

    @CreatedDate
    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    @LastModifiedDate
    @Column(name = "modification_date")
    private LocalDateTime modificationDate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "role_permission",
            schema = "sec",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id")
    )
    private Set<Permission> permissions = new HashSet<>();

}
