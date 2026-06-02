package com.mulikoo.englearnapp;

import com.mulikoo.englearnapp.dto.UserDto;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.Role;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.enums.UserSortField;
import com.mulikoo.englearnapp.exceptions.EntityAlreadyExistsException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.RoleRepository;
import com.mulikoo.englearnapp.repository.UserRepository;
import com.mulikoo.englearnapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserService userService;

    private final UUID userUid = UUID.randomUUID();
    private final UUID categoryUid = UUID.randomUUID();
    private final String username = "testuser";
    private final String password = "password123";
    private final String encodedPassword = "encodedPassword";
    private final String updatedUsername = "updateduser";

    private User createTestUser() {
        User user = new User();
        user.setUid(userUid);
        user.setUsername(username);
        user.setPassword(encodedPassword);
        return user;
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setUid(categoryUid);
        category.setId(1L);
        category.setName("Базовый английский");
        return category;
    }

    private Role createTestRole() {
        Role role = new Role();
        role.setCode("USER");
        return role;
    }

    private UserDto createTestUserDto() {
        UserDto dto = new UserDto();
        dto.setUsername(updatedUsername);
        dto.setCurrentCategoryUid(categoryUid);
        return dto;
    }

    @Test
    void findByUid_ShouldReturnUser_WhenUidExists() {
        User user = createTestUser();
        when(userRepository.findByUid(userUid)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUid(userUid);

        assertThat(result).isPresent().contains(user);
        verify(userRepository).findByUid(userUid);
    }

    @Test
    void findByUid_ShouldReturnEmpty_WhenUidIsNull() {
        Optional<User> result = userService.findByUid(null);
        assertThat(result).isEmpty();
        verify(userRepository, never()).findByUid(any());
    }

    @Test
    void findByUid_ShouldReturnEmpty_WhenUserNotFound() {
        when(userRepository.findByUid(userUid)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUid(userUid);
        assertThat(result).isEmpty();
        verify(userRepository).findByUid(userUid);
    }

    @Test
    void findByUsername_ShouldReturnUser_WhenUsernameExists() {
        User user = createTestUser();
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findByUsername(username);

        assertThat(result).isPresent().contains(user);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUsernameIsNull() {
        Optional<User> result = userService.findByUsername(null);
        assertThat(result).isEmpty();
        verify(userRepository, never()).findByUsername(any());
    }

    @Test
    void findByUsername_ShouldReturnEmpty_WhenUserNotFound() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Optional<User> result = userService.findByUsername(username);
        assertThat(result).isEmpty();
        verify(userRepository).findByUsername(username);
    }

    @Test
    void create_ShouldSaveAndReturnUser_WhenUsernameUniqueAndDefaultCategoryRoleExist() {
        Category category = createTestCategory();
        Role role = createTestRole();
        User savedUser = createTestUser();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Базовый английский")).thenReturn(Optional.of(category));
        when(roleRepository.findByCode("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        Optional<User> result = userService.create(username, password);

        assertThat(result).isPresent().contains(savedUser);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void create_ShouldThrowEntityAlreadyExistsException_WhenUsernameAlreadyExists() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(createTestUser()));

        assertThatThrownBy(() -> userService.create(username, password))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("пользователь с таким username '" + username + "' уже существует");

        verify(categoryRepository, never()).findByName(any());
        verify(roleRepository, never()).findByCode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenDefaultCategoryNotFound() {
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Базовый английский")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.create(username, password))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Не найдена дефолтная категория");

        verify(roleRepository, never()).findByCode(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenDefaultRoleNotFound() {
        Category category = createTestCategory();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Базовый английский")).thenReturn(Optional.of(category));
        when(roleRepository.findByCode("USER")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.create(username, password))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("не найдена дефолтная роль USER");

        verify(userRepository, never()).save(any());
    }

    @Test
    void create_ShouldEncodePasswordBeforeSaving() {
        Category category = createTestCategory();
        Role role = createTestRole();
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(categoryRepository.findByName("Базовый английский")).thenReturn(Optional.of(category));
        when(roleRepository.findByCode("USER")).thenReturn(Optional.of(role));
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        userService.create(username, password);

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertThat(captor.getValue().getPassword()).isEqualTo(encodedPassword);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        when(userRepository.findByUid(userUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(userUid, createTestUserDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("user not found by uid: " + userUid);

        verify(categoryRepository, never()).findIdByUid(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        User existingUser = createTestUser();
        when(userRepository.findByUid(userUid)).thenReturn(Optional.of(existingUser));
        when(categoryRepository.findIdByUid(categoryUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(userUid, createTestUserDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category Not Found with uid: " + categoryUid);

        verify(userRepository, never()).save(any());
    }

    @Test
    void deleteByUid_ShouldCallRepositoryDelete_WhenUidNotNull() {
        userService.deleteByUid(userUid);
        verify(userRepository).deleteByUid(userUid);
    }

    @Test
    void deleteByUid_ShouldDoNothing_WhenUidIsNull() {
        userService.deleteByUid(null);
        verify(userRepository, never()).deleteByUid(any());
    }

    @Test
    void findAll_ShouldReturnPageOfUsers() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "username"));
        Page<User> expectedPage = new PageImpl<>(List.of(createTestUser()));
        when(userRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<User> result = userService.findAll(0, 10, UserSortField.USER_NAME, Sort.Direction.ASC);

        assertThat(result).isEqualTo(expectedPage);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void changeCategory_ShouldUpdateUserCategory_WhenUserAndCategoryExist() {
        Category category = createTestCategory();
        User user = createTestUser();

        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.of(category));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        userService.changeCategory(categoryUid, username);

        assertThat(user.getCurrentCategory()).isEqualTo(category);
        verify(categoryRepository).findByUid(categoryUid);
        verify(userRepository).findByUsername(username);
        // В методе нет вызова save, так как изменение происходит внутри транзакции
    }

    @Test
    void changeCategory_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeCategory(categoryUid, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category Not Found with uid: " + categoryUid);

        verify(userRepository, never()).findByUsername(any());
    }

    @Test
    void changeCategory_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        Category category = createTestCategory();
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.of(category));
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.changeCategory(categoryUid, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Username Not Found with uid: " + username);
    }
}