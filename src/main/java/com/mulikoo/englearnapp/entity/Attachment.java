package com.mulikoo.englearnapp.entity;

import com.mulikoo.englearnapp.enums.MediaType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attachment")
public class Attachment extends BaseEntity {

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "media_type")
    private MediaType mediaType;
}
