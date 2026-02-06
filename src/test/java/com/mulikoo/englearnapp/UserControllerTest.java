package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.UserController;
import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.mapper.UserMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private final static String URL = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserMapper userMapper;

    @DisplayName("получение пользователя по uid")
    @Test
    public void getUsersCase1() throws Exception {
        mockMvc.perform(get("/api/v1/users/{uid}", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("username"));

    }

    @DisplayName("содание нового пользователя")
    @Test
    public void postUsersCase1() throws Exception {
        var requestDto = new UserDto();
        requestDto.setUsername("username");

        mockMvc.perform(
                        post(URL)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.modificationDate").exists());
    }

    @DisplayName("обновление пользователя")
    @Test
    public void putUsersCase1() throws Exception {
        var requestDto = new UserDto();
        requestDto.setUsername("username");

        mockMvc.perform(
                        put("/api/v1/users/{uid}", UUID.randomUUID().toString())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)

                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uid").exists())
                .andExpect(jsonPath("$.username").value("username"))
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.modificationDate").exists());

    }

    @DisplayName("удаление пользователя")
    @Test
    public void deleteUsersCase1() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{uid}", UUID.randomUUID().toString()))
                .andDo(print())
                .andExpect(status().isOk());
    }


}

