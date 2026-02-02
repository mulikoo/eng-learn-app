package com.mulikoo.englearnapp.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "word_attachment")
public class WordAttachment extends CoreEntity {

    @Column("word_id")
    private Long wordId;

    @Column("attachment_id")
    private Long attachmentId;
}
