package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.UserController;
import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.enums.UserSortField;
import com.mulikoo.englearnapp.mapper.UserMapper;
import com.mulikoo.englearnapp.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserController userController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final UUID TEST_UID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID CATEGORY_UID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_PASSWORD = "password123";
    private static final String UPDATED_USERNAME = "updateduser";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    private User createTestUser() {
        User user = new User();
        user.setUid(TEST_UID);
        user.setUsername(TEST_USERNAME);
        user.setPassword(TEST_PASSWORD);
        return user;
    }

    private UserDto createResponseDto() {
        UserDto dto = new UserDto();
        dto.setUid(TEST_UID);
        dto.setUsername(TEST_USERNAME);
        dto.setCurrentCategoryUid(CATEGORY_UID);
        return dto;
    }

    private UserDto createUpdateResponseDto() {
        UserDto dto = new UserDto();
        dto.setUid(TEST_UID);
        dto.setUsername(UPDATED_USERNAME);
        dto.setCurrentCategoryUid(CATEGORY_UID);
        return dto;
    }

    private UserDto createUpdateRequestDto() {
        UserDto dto = new UserDto();
        dto.setUsername(UPDATED_USERNAME);
        dto.setCurrentCategoryUid(CATEGORY_UID);
        return dto;
    }


    @Test
    void getUser_shouldReturnUserDto_whenUserExists() throws Exception {
        User user = createTestUser();
        UserDto dto = createResponseDto();

        when(userService.findByUid(TEST_UID)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/users/{uid}", TEST_UID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(TEST_UID.toString()))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
    }

    @Test
    void getUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        when(userService.findByUid(TEST_UID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/users/{uid}", TEST_UID))
                .andExpect(status().isNotFound());
    }

    @Test
    void createUser_shouldReturnCreatedUser() throws Exception {
        User savedUser = createTestUser();
        UserDto responseDto = createResponseDto();

        when(userService.create(eq(TEST_USERNAME), eq(TEST_PASSWORD)))
                .thenReturn(Optional.of(savedUser));
        when(userMapper.toDto(savedUser)).thenReturn(responseDto);

        mockMvc.perform(post("/api/v1/users")
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uid").value(TEST_UID.toString()))
                .andExpect(jsonPath("$.username").value(TEST_USERNAME));
    }

    @Test
    void createUser_shouldReturnBadRequest_whenServiceReturnsEmpty() throws Exception {
        when(userService.create(eq(TEST_USERNAME), eq(TEST_PASSWORD)))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/api/v1/users")
                        .param("username", TEST_USERNAME)
                        .param("password", TEST_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUser_shouldReturnUpdatedUser() throws Exception {
        User updatedUser = createTestUser();
        updatedUser.setUsername(UPDATED_USERNAME);
        UserDto responseDto = createUpdateResponseDto();

        when(userService.update(eq(TEST_UID), any(UserDto.class))).thenReturn(Optional.of(updatedUser));
        when(userMapper.toDto(updatedUser)).thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(createUpdateRequestDto());

        mockMvc.perform(put("/api/v1/users/{uid}", TEST_UID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value(UPDATED_USERNAME));
    }

    @Test
    void updateUser_shouldReturnNotFound_whenUserDoesNotExist() throws Exception {
        when(userService.update(eq(TEST_UID), any(UserDto.class))).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(createUpdateRequestDto());

        mockMvc.perform(put("/api/v1/users/{uid}", TEST_UID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteUser_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/users/{uid}", TEST_UID))
                .andExpect(status().isNoContent());
    }
}
