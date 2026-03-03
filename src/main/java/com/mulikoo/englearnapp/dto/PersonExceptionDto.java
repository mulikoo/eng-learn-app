package com.mulikoo.englearnapp.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PersonExceptionDto {

    private String message;
    private String error;

    public PersonExceptionDto(String message, String error) {
        this.message = message;
        this.error = error;
    }
}
