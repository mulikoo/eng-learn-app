package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.AuthResponseDto;
import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.dto.UserRegisterDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.mapper.UserMapper;
import com.mulikoo.englearnapp.service.AuthService;
import com.mulikoo.englearnapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/auth/v1")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "контроллер")
@RestController
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Создание нового пользователя", description = "Создание нового пользователя")
    public ResponseEntity<UserDto> createUser(@Validated @RequestBody UserRegisterDto userRegisterDto) {
        log.info("создание нового пользователя. получили username{}", userRegisterDto.username());

        Optional<User> result = userService.create(userRegisterDto.username(), userRegisterDto.password());
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userMapper.toDto(result.get()), HttpStatus.CREATED);
    }

    @PostMapping("/token")
    public ResponseEntity<AuthResponseDto> getToken(@AuthenticationPrincipal UserDetails userDetails) {
        log.info("попытка получения токена для пользователя " + userDetails.getUsername());

        AuthResponseDto result = authService.generateToken(userDetails);

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
