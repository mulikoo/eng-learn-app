package com.mulikoo.englearnapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table("attachment")
public class Attachment extends BaseEntity {

    @Column("file_url")
    private String fileUrl;

    private MediaType mediaType;
}
