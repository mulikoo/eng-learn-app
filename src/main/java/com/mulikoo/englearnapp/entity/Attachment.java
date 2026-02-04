package com.mulikoo.englearnapp.entity;

import com.mulikoo.englearnapp.enums.MediaType;
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

    @Column("media_type")
    private MediaType mediaType;
}
