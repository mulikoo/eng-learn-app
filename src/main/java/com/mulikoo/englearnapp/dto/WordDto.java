package com.mulikoo.englearnapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "слово")
public class WordDto {

    @Schema(description = "uid - уникальный индентификатор", example = "143e4567-e79b-24d3-a456-426614174111")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @Schema(description = "само слово", example = "apple")
    private String name;

    @Schema(description = "перевод слова", example = "яблоко")
    private String translation;

    @Schema(description = "подсказка", example = "круглый фрукт")
    private String clue;

    @Schema(description = "uid ссылка на категорию", example = "321e4468-e89b-12d3-a456-426614174000")
    private UUID categoryUid;

    @Schema(description = "дата создания", example = "2026-02-21")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(description = "дата изменения", example = "2026-02-22")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modificationDate;
}
