package com.mulikoo.englearnapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseEntity extends CoreEntity {

    @Column("uid")
    protected UUID uid;

    @Column("creation_date")
    protected LocalDateTime creationDate;

    @Column("modification_date")
    protected LocalDateTime modificationDate;

}
