package com.mulikoo.englearnapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity extends CoreEntity {

    @Column(name = "uid")
    protected UUID uid;

    @Column(name = "creation_date")
    protected LocalDateTime creationDate;

    @Column(name = "modification_date")
    protected LocalDateTime modificationDate;

}
