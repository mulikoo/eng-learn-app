package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.enums.UserSortField;
import com.mulikoo.englearnapp.mapper.UserMapper;
import com.mulikoo.englearnapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@Tag(name = "Контроллер для пользователей", description = "Контроллер для управление пользователями")
@Validated
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping("/{uid}")
    @Operation(summary = "Получение пользователя по uid", description = "Возвращает пользователя")
    public ResponseEntity<UserDto> getUser(@Parameter(description = "uid пользвателя") @NotNull @PathVariable("uid") UUID uid) {
        log.info("попытка получения пользователя по uid: {}", uid.toString());

        Optional<User> result = userService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(result.get()));
    }

    @PreAuthorize("hasAuthority('USER_READ')")
    @GetMapping
    @Operation(summary = "Получение списка пользователей", description = "Возвращает список пользователей")
    public ResponseEntity<Page<UserDto>> getAllUser(@RequestParam(name = "page", defaultValue = "0") @Min(0) int page,
                                                    @RequestParam(name = "size", defaultValue = "10") @Min(1) int size,
                                                    @RequestParam(name = "sortField", defaultValue = "USER_NAME") UserSortField userSortField,
                                                    @RequestParam(name = "sortDirection", defaultValue = "ASC") Sort.Direction sortDirection) {
        log.info("попытка получения списка пользователей");

        Page<User> userPage = userService.findAll(page, size, userSortField, sortDirection);
        Page<UserDto> userDtoPage = userPage.map(userMapper::toDto);

        return ResponseEntity.ok(userDtoPage);
    }

    @PreAuthorize("hasAuthority('USER_CREATE')")
    @PostMapping
    @Operation(summary = "Создание нового пользователя", description = "Создание нового пользователя")
    public ResponseEntity<UserDto> createUser(@RequestParam(name = "username") String username,
                                              @RequestParam(name = "password") String password) {
        log.info("создание нового пользователя. получили username{}", username);

        Optional<User> result = userService.create(username, password);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userMapper.toDto(result.get()), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAuthority('USER_UPDATE')")
    @PutMapping("/{uid}")
    @Operation(summary = "Обновление пользователя", description = "Позволяет обновлять пользователя")
    public ResponseEntity<UserDto> updateUser(@Parameter(description = "uid пользователя")
                                              @NotNull @PathVariable("uid") UUID uid, @Validated @RequestBody UserDto userDto) {
        log.info("обновление пользователя по uid: {}", uid.toString());

        Optional<User> resulte = userService.update(uid, userDto);
        if (resulte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(resulte.get()));

    }

    @PreAuthorize("hasAuthority('USER_DELETE')")
    @DeleteMapping("/{uid}")
    @Operation(summary = "Удаление пользователя", description = "Позволяет удалять пользователя")
    public ResponseEntity<UserDto> deleteUser(@Parameter(description = "uid категории") @NotNull @PathVariable("uid") UUID uid) {
        log.info("удаление пользователя по uid: {}", uid.toString());

        userService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAuthority('USER_CATEGORY_CHANGE')")
    @PatchMapping("/categoryChange")
    @Operation(summary = "Изменение категории слова у юзера", description = "Изменяет категорию слова")
    public ResponseEntity<Void> categoryChange(@RequestParam UUID uidCategory,
                                               @AuthenticationPrincipal Jwt token) {
        log.info("Изменение категории слова у юзера");

        userService.changeCategory(uidCategory, token.getSubject());

        return ResponseEntity.ok().build();
    }

}

