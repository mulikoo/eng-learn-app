package com.mulikoo.englearnapp;

import com.mulikoo.englearnapp.dto.PhraseDto;
import com.mulikoo.englearnapp.dto.view.UserProgressViewPhrase;
import com.mulikoo.englearnapp.entity.*;
import com.mulikoo.englearnapp.enums.ClueType;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import com.mulikoo.englearnapp.exceptions.*;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.PhraseRepository;
import com.mulikoo.englearnapp.service.PhraseService;
import com.mulikoo.englearnapp.service.UserProgressService;
import com.mulikoo.englearnapp.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PhraseServiceTest {

    @Mock
    private PhraseRepository phraseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserProgressService userProgressService;
    @Mock
    private UserService userService;

    @InjectMocks
    private PhraseService phraseService;

    private final UUID phraseUid = UUID.randomUUID();
    private final UUID categoryUid = UUID.randomUUID();
    private final String username = "testUser";
    private final String phraseName = "Break a leg";
    private final String translation = "Ни пуха ни пера";
    private final String clue = "пожелание удачи";

    private Phrase createTestPhrase() {
        Phrase phrase = new Phrase();
        phrase.setUid(phraseUid);
        phrase.setName(phraseName);
        phrase.setTranslation(translation);
        phrase.setClue(clue);
        return phrase;
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setUid(categoryUid);
        category.setName("Idioms");
        return category;
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername(username);
        user.setCurrentCategory(createTestCategory());
        return user;
    }

    private PhraseDto createTestPhraseDto() {
        PhraseDto dto = new PhraseDto();
        dto.setName(phraseName);
        dto.setTranslation(translation);
        dto.setClue(clue);
        dto.setCategoryUid(categoryUid);
        return dto;
    }

    @Test
    void findByUid_ShouldReturnPhrase_WhenUidExists() {
        Phrase phrase = createTestPhrase();
        when(phraseRepository.findByUid(phraseUid)).thenReturn(Optional.of(phrase));

        Optional<Phrase> result = phraseService.findByUid(phraseUid);

        assertThat(result).isPresent().contains(phrase);
        verify(phraseRepository).findByUid(phraseUid);
    }

    @Test
    void findByUid_ShouldReturnEmpty_WhenUidIsNull() {
        Optional<Phrase> result = phraseService.findByUid(null);
        assertThat(result).isEmpty();
        verify(phraseRepository, never()).findByUid(any());
    }

    @Test
    void findByUid_ShouldReturnEmpty_WhenPhraseNotFound() {
        when(phraseRepository.findByUid(phraseUid)).thenReturn(Optional.empty());

        Optional<Phrase> result = phraseService.findByUid(phraseUid);
        assertThat(result).isEmpty();
        verify(phraseRepository).findByUid(phraseUid);
    }

    @Test
    void create_ShouldSaveAndReturnPhrase_WhenDataValid() {
        PhraseDto dto = createTestPhraseDto();
        Category category = createTestCategory();
        Phrase savedPhrase = createTestPhrase();

        when(phraseRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())).thenReturn(false);
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.of(category));
        when(phraseRepository.save(any(Phrase.class))).thenReturn(savedPhrase);

        Optional<Phrase> result = phraseService.create(dto);

        assertThat(result).isPresent().contains(savedPhrase);
        verify(phraseRepository).save(any(Phrase.class));
    }

    @Test
    void create_ShouldThrowEntityAlreadyExistsException_WhenPhraseDuplicate() {
        PhraseDto dto = createTestPhraseDto();
        when(phraseRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())).thenReturn(true);

        assertThatThrownBy(() -> phraseService.create(dto))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("фраза уже существует");

        verify(categoryRepository, never()).findByUid(any());
        verify(phraseRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        PhraseDto dto = createTestPhraseDto();
        when(phraseRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())).thenReturn(false);
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.create(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category Not Found");

        verify(phraseRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateAndReturnPhrase_WhenPhraseAndCategoryExist() {
        Phrase existingPhrase = createTestPhrase();
        PhraseDto updateDto = createTestPhraseDto();
        updateDto.setName("Updated phrase");
        Category category = createTestCategory();
        Phrase updatedPhrase = createTestPhrase();
        updatedPhrase.setName("Updated phrase");

        when(phraseRepository.findByUid(phraseUid)).thenReturn(Optional.of(existingPhrase));
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.of(category));
        when(phraseRepository.save(any(Phrase.class))).thenReturn(updatedPhrase);

        Optional<Phrase> result = phraseService.update(phraseUid, updateDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("Updated phrase");
        verify(phraseRepository).save(existingPhrase);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenPhraseNotFound() {
        when(phraseRepository.findByUid(phraseUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.update(phraseUid, createTestPhraseDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("фразы не существует по uid");

        verify(categoryRepository, never()).findByUid(any());
        verify(phraseRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        Phrase existingPhrase = createTestPhrase();
        when(phraseRepository.findByUid(phraseUid)).thenReturn(Optional.of(existingPhrase));
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.update(phraseUid, createTestPhraseDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category Not Found");

        verify(phraseRepository, never()).save(any());
    }

    @Test
    void deleteByUid_ShouldCallRepositoryDelete_WhenUidNotNull() {
        phraseService.deleteByUid(phraseUid);
        verify(phraseRepository).deleteByUid(phraseUid);
    }

    @Test
    void deleteByUid_ShouldDoNothing_WhenUidIsNull() {
        phraseService.deleteByUid(null);
        verify(phraseRepository, never()).deleteByUid(any());
    }

    @Test
    void findNextPhrase_ShouldReturnPhraseAndRegisterProgress_WhenNoInProgressAndPhraseExists() {
        User user = createTestUser();
        List<UserProgressViewPhrase> learnedList = List.of(); // нет изученных/в процессе
        Phrase nextPhrase = createTestPhrase();

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userProgressService.findPhraseIdByUser(user, user.getCurrentCategory())).thenReturn(learnedList);
        when(phraseRepository.findNextByUserCategory(user.getCurrentCategory(), List.of())).thenReturn(Optional.of(nextPhrase));

        Optional<Phrase> result = phraseService.findNextPhrase(username);

        assertThat(result).isPresent().contains(nextPhrase);
        verify(userProgressService).registerUserProgressForPhrase(user, nextPhrase);
    }

    @Test
    void findNextPhrase_ShouldThrowHasWordAlreadyInLearningException_WhenInProgressExists() {
        User user = createTestUser();
        UserProgressViewPhrase inProgressView = mock(UserProgressViewPhrase.class);
        when(inProgressView.getStatus()).thenReturn(UserProgressStatus.IN_PROGRESS);
        List<UserProgressViewPhrase> learnedList = List.of(inProgressView);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userProgressService.findPhraseIdByUser(user, user.getCurrentCategory())).thenReturn(learnedList);

        assertThatThrownBy(() -> phraseService.findNextPhrase(username))
                .isInstanceOf(HasWordAlreadyInLearningException.class)
                .hasMessageContaining("уже есть фразы на изучении");

        verify(phraseRepository, never()).findNextByUserCategory(any(), any());
        verify(userProgressService, never()).registerUserProgressForPhrase(any(), any());
    }

    @Test
    void findNextPhrase_ShouldThrowAvailableWordsNotFoundInCategoryException_WhenNoPhrasesLeft() {
        User user = createTestUser();
        List<UserProgressViewPhrase> learnedList = List.of();

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userProgressService.findPhraseIdByUser(user, user.getCurrentCategory())).thenReturn(learnedList);
        when(phraseRepository.findNextByUserCategory(user.getCurrentCategory(), List.of())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.findNextPhrase(username))
                .isInstanceOf(AvailableWordsNotFoundInCategoryException.class)
                .hasMessageContaining("фразы по данной категории");

        verify(userProgressService, never()).registerUserProgressForPhrase(any(), any());
    }

    @Test
    void findNextPhrase_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.findNextPhrase(username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void isTranslationCorrect_ShouldReturnTrueAndCallProgress_WhenTranslationMatches() {
        when(phraseRepository.findTranslationByUid(phraseUid)).thenReturn(Optional.of(translation));

        boolean result = phraseService.isTranslationCorrect(phraseUid, translation, username);

        assertThat(result).isTrue();
        verify(userProgressService).updateProgressPhrase(phraseUid, username, true);
    }

    @Test
    void isTranslationCorrect_ShouldReturnFalseAndCallProgress_WhenTranslationMismatch() {
        when(phraseRepository.findTranslationByUid(phraseUid)).thenReturn(Optional.of(translation));

        boolean result = phraseService.isTranslationCorrect(phraseUid, "wrong", username);

        assertThat(result).isFalse();
        verify(userProgressService).updateProgressPhrase(phraseUid, username, false);
    }

    @Test
    void isTranslationCorrect_ShouldThrowEntityNotFoundException_WhenPhraseNotFound() {
        when(phraseRepository.findTranslationByUid(phraseUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.isTranslationCorrect(phraseUid, translation, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Phrase not found");
    }

    @Test
    void getClue_ShouldReturnClueAndAddTextClueType_WhenProgressExistsAndClueTypesNull() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(null);
        when(userProgressService.getUserProgressByPhraseUid(phraseUid, username)).thenReturn(Optional.of(progress));
        when(phraseRepository.findClueByUid(phraseUid)).thenReturn(Optional.of(clue));

        String result = phraseService.getClue(phraseUid, username);

        assertThat(result).isEqualTo(clue);
        assertThat(progress.getUserClueTypes()).containsExactly(ClueType.TEXT);
    }

    @Test
    void getClue_ShouldAddTextClueType_WhenUserClueTypesDoesNotContainText() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(new HashSet<>());
        when(userProgressService.getUserProgressByPhraseUid(phraseUid, username)).thenReturn(Optional.of(progress));
        when(phraseRepository.findClueByUid(phraseUid)).thenReturn(Optional.of(clue));

        String result = phraseService.getClue(phraseUid, username);

        assertThat(result).isEqualTo(clue);
        assertThat(progress.getUserClueTypes()).containsExactly(ClueType.TEXT);
    }

    @Test
    void getClue_ShouldNotAddDuplicateTextClueType_WhenAlreadyContains() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(new HashSet<>(Set.of(ClueType.TEXT)));
        when(userProgressService.getUserProgressByPhraseUid(phraseUid, username)).thenReturn(Optional.of(progress));
        when(phraseRepository.findClueByUid(phraseUid)).thenReturn(Optional.of(clue));

        String result = phraseService.getClue(phraseUid, username);

        assertThat(result).isEqualTo(clue);
        assertThat(progress.getUserClueTypes()).hasSize(1);
    }

    @Test
    void getClue_ShouldThrowEntityNotFoundException_WhenProgressNotFound() {
        when(userProgressService.getUserProgressByPhraseUid(phraseUid, username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.getClue(phraseUid, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ещё не начато изучение");
    }

    @Test
    void getClue_ShouldThrowEntityNotFoundException_WhenClueNotFound() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(new HashSet<>());
        when(userProgressService.getUserProgressByPhraseUid(phraseUid, username)).thenReturn(Optional.of(progress));
        when(phraseRepository.findClueByUid(phraseUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> phraseService.getClue(phraseUid, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("подсказка для фразы с uid");
    }
}