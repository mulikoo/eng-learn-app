package com.mulikoo.englearnapp.dto;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class AuthResponseDto {
    private final String accessToken;
    public final Long expiresIn;

    public AuthResponseDto(@NonNull String accessToken, @NonNull Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
