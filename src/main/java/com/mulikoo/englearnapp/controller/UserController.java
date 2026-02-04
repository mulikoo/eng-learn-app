package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;

    @GetMapping("/{uid}")
    public ResponseEntity<UserDto> getUser(@PathVariable("uid") UUID uid) {

        log.info("попытка получения пользователя по uid: {}", uid.toString());

        var mockResponse = new UserDto();
        mockResponse.setUid(uid);
        mockResponse.setUsername("username");
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return ResponseEntity.ok(mockResponse);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("создание нового пользователя. получили username{}", userDto.getUsername());

        var mockResponse = new UserDto();
        mockResponse.setUsername(userDto.getUsername());
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return new ResponseEntity<>(mockResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{uid}")
    public ResponseEntity<UserDto> updateUser(@PathVariable UUID uid, @RequestBody UserDto userDto) {
        log.info("обновление пользователя по uid: {}", uid.toString());

        var mockResponse = new UserDto();
        mockResponse.setUid(uid);
        mockResponse.setCreationDate(LocalDateTime.now());
        mockResponse.setModificationDate(LocalDateTime.now());

        return ResponseEntity.ok(mockResponse);
    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable UUID uid) {
        log.info("удаление пользователя по uid: {}", uid.toString());

        return ResponseEntity.ok().build();
    }
}

