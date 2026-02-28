package com.mulikoo.englearnapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "категория")
public class CategoryDto {
    @Schema(description = "uid - уникальный индентификатор", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @NotBlank(message = "название категории не может быть пустым")
    @Schema(description = "название категории", example = "бизнес английский")
    private String name;

    @NotBlank(message = "описание категории не может быть пустым")
    @Schema(description = "описание категории", example = "английский для работы в коллективе")
    private String description;

    @Schema(description = "дата создания", example = "2026-02-15T20:41:45.104673")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(description = "дата изменения", example = "2026-02-15T20:41:45.104673")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modificationDate;
}
