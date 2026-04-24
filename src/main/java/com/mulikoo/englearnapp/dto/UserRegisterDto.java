package com.mulikoo.englearnapp.dto;

import jakarta.validation.constraints.NotBlank;
public record UserRegisterDto(@NotBlank(message = "юзернейм не может быть пустым") String username,
                              @NotBlank(message = "пароль не может быть пустым") String password) {


}
