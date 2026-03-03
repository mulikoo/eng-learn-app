package com.mulikoo.englearnapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorResponseDto {

    private String message;
    private String error;

    public ErrorResponseDto(String message, String error) {
        this.message = message;
        this.error = error;
    }
}