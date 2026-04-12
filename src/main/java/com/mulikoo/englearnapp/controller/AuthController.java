package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.mapper.UserMapper;
import com.mulikoo.englearnapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth/v1")
@Validated
@RequiredArgsConstructor
@Slf4j
@Tag(name = "контроллер")
@RestController
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("/register")
    @Operation(summary = "Создание нового пользователя", description = "Создание нового пользователя")
    public ResponseEntity<UserDto> createUser(@RequestParam(name = "username") String username,
                                              @RequestParam(name = "password") String password) {
        log.info("создание нового пользователя. получили username{}", username);

        User result = userService.create(username, password);
        if (result == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userMapper.toDto(result), HttpStatus.CREATED);
    }
}
