package com.mulikoo.englearnapp.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "пользователь")
public class UserDto {

    @Schema(description = "uid - уникальный индентификатор", example = "123e4567-e89b-12d3-a456-426614174000")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private UUID uid;

    @Schema(description = "юзернейм пользователя", example = "ivan006")
    private String username;

    @Schema(description = "uid ссылка на категорию", example = "321e4468-e89b-12d3-a456-426614174000")
    private UUID currentCategoryUid;

    @Schema(description = "дата создания", example = "2026-02-21")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime creationDate;

    @Schema(description = "дата изменения", example = "2026-02-22")
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDateTime modificationDate;

}
