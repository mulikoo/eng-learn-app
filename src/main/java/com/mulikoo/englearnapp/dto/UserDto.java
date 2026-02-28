package com.mulikoo.englearnapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "пользователь")
public class UserDto {

    @Schema(description = "uid - уникальный индентификатор", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @NotBlank(message = "имя пользователя не может быть пустым")
    @Size(min = 5, max = 32)
    @Schema(description = "юзернейм пользователя", example = "ivan006")
    private String username;

    @NotNull
    @Schema(description = "uid ссылка на категорию", example = "321e4468-e89b-12d3-a456-426614174000")
    private UUID currentCategoryUid;

    @Schema(description = "дата создания", example = "2026-02-15T20:41:45.104673")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(description = "дата изменения", example = "2026-02-15T20:41:45.104673")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modificationDate;

}
