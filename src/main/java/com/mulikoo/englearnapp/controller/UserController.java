package com.mulikoo.englearnapp.controller;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.mapper.UserMapper;
import com.mulikoo.englearnapp.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RequestMapping("/api/v1/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserMapper userMapper;
    private final UserService userService;

    @GetMapping("/{uid}")
    public ResponseEntity<UserDto> getUser(@PathVariable("uid") UUID uid) {
        log.info("попытка получения пользователя по uid: {}", uid.toString());

        Optional<User> result = userService.findByUid(uid);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(result.get()));
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        log.info("создание нового пользователя. получили username{}", userDto.getUsername());

        Optional<User> result = userService.create(userDto);
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(userMapper.toDto(result.get()), HttpStatus.CREATED);

    }

    //public ResponseEntity<WordDto> createWord(@RequestBody WordDto wordDto) {
    //        log.info("создание нового слова. получили name:{}, translation: {}",
    //                wordDto.getName(), wordDto.getTranslation());
    //
    //        Optional<Word> result = wordService.create(wordDto);
    //        if (result.isEmpty()) {
    //            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    //        }
    //        return new ResponseEntity<>(wordMapper.toDto(result.get()), HttpStatus.CREATED);
    //    }

    @PutMapping("/{uid}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("uid") UUID uid, @RequestBody UserDto userDto) {
        log.info("обновление пользователя по uid: {}", uid.toString());

        Optional<User> resulte =  userService.update(uid, userDto);
        if (resulte.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(userMapper.toDto(resulte.get()));

    }

    @DeleteMapping("/{uid}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable("uid") UUID uid) {
        log.info("удаление пользователя по uid: {}", uid.toString());

        userService.deleteByUid(uid);

        return ResponseEntity.noContent().build();
    }
}

