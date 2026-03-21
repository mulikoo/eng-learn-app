package com.mulikoo.englearnapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class CoreEntity {

    @Id
    @Column(name = "id")
    protected Long id;
}
