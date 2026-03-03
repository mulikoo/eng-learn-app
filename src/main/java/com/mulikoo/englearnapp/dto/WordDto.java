package com.mulikoo.englearnapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "слово")
public class WordDto {

    @Schema(description = "uid - уникальный индентификатор", example = "143e4567-e79b-24d3-a456-426614174111")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @NotBlank(message = "слово не может быть пустым")
    @Schema(description = "само слово", example = "apple")
    private String name;

    @NotBlank(message = "перевод не может быть пустым")
    @Schema(description = "перевод слова", example = "яблоко")
    private String translation;

    @NotBlank(message = "подсказка не может быть пустой")
    @Schema(description = "подсказка", example = "круглый фрукт")
    private String clue;

    @NotNull(message = "uid категории не может быть пустым")
    @Schema(description = "uid ссылка на категорию", example = "321e4468-e89b-12d3-a456-426614174000")
    private UUID categoryUid;

    @Schema(description = "дата создания", example = "2026-02-15T20:41:45.104673")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(description = "дата изменения", example = "2026-02-15T20:41:45.104673")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modificationDate;
}
