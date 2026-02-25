package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.mapper.UserMapper;
import com.mulikoo.englearnapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@Tag(name = "Контроллер для пользователей", description = "Контроллер для управление пользователями")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/{uid}")
    @Operation(summary = "Получение пользователя по uid", description = "Возвращает пользователя")
    public ResponseEntity<UserDto> getUser(@Parameter(description = "uid пользвателя") @PathVariable("uid") UUID uid) {
        log.info("попытка получения пользователя по uid: {}", uid.toString());

        Optional<User> result = userService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(result.get()));
    }

    @PostMapping
    @Operation(summary = "Создание нового пользователя", description = "Создание нового пользователя по uid")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("создание нового пользователя. получили username{}", userDto.getUsername());

        Optional<User> result = userService.create(userDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userMapper.toDto(result.get()), HttpStatus.CREATED);

    }

    @PutMapping("/{uid}")
    @Operation(summary = "Обновление пользователя", description = "Позволяет обновлять пользователя")
    public ResponseEntity<UserDto> updateUser(@Parameter(description = "uid пользователя")
                                              @PathVariable("uid") UUID uid, @RequestBody UserDto userDto) {
        log.info("обновление пользователя по uid: {}", uid.toString());

        Optional<User> resulte = userService.update(uid, userDto);
        if (resulte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(resulte.get()));

    }

    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление пользователя", description = "Позволяет удалять польхователя")
    public ResponseEntity<UserDto> deleteUser(@Parameter(description = "uid категории") @PathVariable("uid") UUID uid) {
        log.info("удаление пользователя по uid: {}", uid.toString());

        userService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }
}

