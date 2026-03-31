package com.mulikoo.englearnapp.entity;

import com.mulikoo.englearnapp.enums.MediaType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "attachment")
public class Attachment extends BaseEntity {

    @Column(name = "file_url", unique = true, nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "media_type", nullable = false)
    private MediaType mediaType;
}
