package com.mulikoo.englearnapp;

import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.dto.view.UserProgressView;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.entity.UserProgress;
import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.enums.ClueType;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import com.mulikoo.englearnapp.enums.WordSortField;
import com.mulikoo.englearnapp.exceptions.*;
import com.mulikoo.englearnapp.repository.CategoryRepository;
import com.mulikoo.englearnapp.repository.UserProgressRepository;
import com.mulikoo.englearnapp.repository.WordRepository;
import com.mulikoo.englearnapp.service.UserProgressService;
import com.mulikoo.englearnapp.service.UserService;
import com.mulikoo.englearnapp.service.WordService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WordServiceTest {

    @Mock
    private WordRepository wordRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private UserService userService;
    @Mock
    private UserProgressService userProgressService;
    @Mock
    private UserProgressRepository userProgressRepository;

    @InjectMocks
    private WordService wordService;

    private final UUID wordUid = UUID.randomUUID();
    private final UUID categoryUid = UUID.randomUUID();
    private final String username = "testUser";
    private final String wordName = "apple";
    private final String translation = "яблоко";
    private final String clue = "красный фрукт";

    private Word createTestWord() {
        Word word = new Word();
        word.setUid(wordUid);
        word.setName(wordName);
        word.setTranslation(translation);
        word.setClue(clue);
        return word;
    }

    private Category createTestCategory() {
        Category category = new Category();
        category.setUid(categoryUid);
        category.setName("Fruits");
        return category;
    }

    private User createTestUser() {
        User user = new User();
        user.setUsername(username);
        user.setCurrentCategory(createTestCategory());
        return user;
    }

    private WordDto createTestWordDto() {
        WordDto dto = new WordDto();
        dto.setName(wordName);
        dto.setTranslation(translation);
        dto.setClue(clue);
        dto.setCategoryUid(categoryUid);
        return dto;
    }

    @Test
    void findByUid_ShouldReturnWord_WhenUidExists() {
        Word word = createTestWord();
        when(wordRepository.findByUid(wordUid)).thenReturn(Optional.of(word));

        Optional<Word> result = wordService.findByUid(wordUid);

        assertThat(result).isPresent().contains(word);
        verify(wordRepository).findByUid(wordUid);
    }

    @Test
    void findByUid_ShouldReturnEmpty_WhenUidIsNull() {
        Optional<Word> result = wordService.findByUid(null);
        assertThat(result).isEmpty();
        verify(wordRepository, never()).findByUid(any());
    }

    @Test
    void findByUid_ShouldReturnEmpty_WhenWordNotFound() {
        when(wordRepository.findByUid(wordUid)).thenReturn(Optional.empty());

        Optional<Word> result = wordService.findByUid(wordUid);
        assertThat(result).isEmpty();
        verify(wordRepository).findByUid(wordUid);
    }

    @Test
    void create_ShouldSaveAndReturnWord_WhenDataValid() {
        WordDto dto = createTestWordDto();
        Category category = createTestCategory();
        Word savedWord = createTestWord();

        when(wordRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())).thenReturn(false);
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.of(category));
        when(wordRepository.save(any(Word.class))).thenReturn(savedWord);

        Optional<Word> result = wordService.create(dto);

        assertThat(result).isPresent().contains(savedWord);
        verify(wordRepository).save(any(Word.class));
    }

    @Test
    void create_ShouldThrowEntityAlreadyExistsException_WhenWordDuplicate() {
        WordDto dto = createTestWordDto();
        when(wordRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())).thenReturn(true);

        assertThatThrownBy(() -> wordService.create(dto))
                .isInstanceOf(EntityAlreadyExistsException.class)
                .hasMessageContaining("word уже существует");

        verify(categoryRepository, never()).findByUid(any());
        verify(wordRepository, never()).save(any());
    }

    @Test
    void create_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        WordDto dto = createTestWordDto();
        when(wordRepository.existsByNameAndTranslation(dto.getName(), dto.getTranslation())).thenReturn(false);
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.create(dto))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category Not Found");

        verify(wordRepository, never()).save(any());
    }

    @Test
    void update_ShouldUpdateAndReturnWord_WhenWordAndCategoryExist() {
        Word existingWord = createTestWord();
        WordDto updateDto = createTestWordDto();
        updateDto.setName("newName");
        Category category = createTestCategory();
        Word updatedWord = createTestWord();
        updatedWord.setName("newName");

        when(wordRepository.findByUid(wordUid)).thenReturn(Optional.of(existingWord));
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.of(category));
        when(wordRepository.save(any(Word.class))).thenReturn(updatedWord);

        Optional<Word> result = wordService.update(wordUid, updateDto);

        assertThat(result).isPresent();
        assertThat(result.get().getName()).isEqualTo("newName");
        verify(wordRepository).save(existingWord);
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenWordNotFound() {
        when(wordRepository.findByUid(wordUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.update(wordUid, createTestWordDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("слово не существует по uid");

        verify(categoryRepository, never()).findByUid(any());
        verify(wordRepository, never()).save(any());
    }

    @Test
    void update_ShouldThrowEntityNotFoundException_WhenCategoryNotFound() {
        Word existingWord = createTestWord();
        when(wordRepository.findByUid(wordUid)).thenReturn(Optional.of(existingWord));
        when(categoryRepository.findByUid(categoryUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.update(wordUid, createTestWordDto()))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Category Not Found");

        verify(wordRepository, never()).save(any());
    }

    @Test
    void deleteByUid_ShouldCallRepositoryDelete_WhenUidNotNull() {
        wordService.deleteByUid(wordUid);
        verify(wordRepository).deleteByUid(wordUid);
    }

    @Test
    void deleteByUid_ShouldDoNothing_WhenUidIsNull() {
        wordService.deleteByUid(null);
        verify(wordRepository, never()).deleteByUid(any());
    }

    @Test
    void findAll_ShouldReturnPageOfWords() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "name"));
        Page<Word> expectedPage = new PageImpl<>(List.of(createTestWord()));
        when(wordRepository.findAll(pageable)).thenReturn(expectedPage);

        Page<Word> result = wordService.findAll(0, 10, WordSortField.NAME, Sort.Direction.ASC);

        assertThat(result).isEqualTo(expectedPage);
        verify(wordRepository).findAll(pageable);
    }

    @Test
    void findNextWord_ShouldReturnWordAndRegisterProgress_WhenNoInProgressAndWordExists() {
        User user = createTestUser();
        List<UserProgressView> learnedList = List.of(); // нет изученных/в процессе
        Word nextWord = createTestWord();

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userProgressService.findWordIdsByUser(user, user.getCurrentCategory())).thenReturn(learnedList);
        when(wordRepository.findNextByUserCategory(user.getCurrentCategory(), List.of())).thenReturn(Optional.of(nextWord));

        Optional<Word> result = wordService.findNextWord(username);

        assertThat(result).isPresent().contains(nextWord);
        verify(userProgressService).registerUserProgress(user, nextWord);
    }

    @Test
    void findNextWord_ShouldThrowHasWordAlreadyInLearningException_WhenInProgressExists() {
        User user = createTestUser();
        UserProgressView inProgressView = mock(UserProgressView.class);
        when(inProgressView.getStatus()).thenReturn(UserProgressStatus.IN_PROGRESS);
        List<UserProgressView> learnedList = List.of(inProgressView);

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userProgressService.findWordIdsByUser(user, user.getCurrentCategory())).thenReturn(learnedList);

        assertThatThrownBy(() -> wordService.findNextWord(username))
                .isInstanceOf(HasWordAlreadyInLearningException.class)
                .hasMessageContaining("уже есть слова на изучении");

        verify(wordRepository, never()).findNextByUserCategory(any(), any());
        verify(userProgressService, never()).registerUserProgress(any(), any());
    }

    @Test
    void findNextWord_ShouldThrowAvailableWordsNotFoundInCategoryException_WhenNoWordsLeft() {
        User user = createTestUser();
        List<UserProgressView> learnedList = List.of();

        when(userService.findByUsername(username)).thenReturn(Optional.of(user));
        when(userProgressService.findWordIdsByUser(user, user.getCurrentCategory())).thenReturn(learnedList);
        when(wordRepository.findNextByUserCategory(user.getCurrentCategory(), List.of())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.findNextWord(username))
                .isInstanceOf(AvailableWordsNotFoundInCategoryException.class)
                .hasMessageContaining("Слова по данной категории");

        verify(userProgressService, never()).registerUserProgress(any(), any());
    }

    @Test
    void findNextWord_ShouldThrowEntityNotFoundException_WhenUserNotFound() {
        when(userService.findByUsername(username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.findNextWord(username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("User not found");
    }

    @Test
    void isTranslationCorrect_ShouldReturnTrueAndCallProgress_WhenTranslationMatches() {
        when(wordRepository.findTranslationByUid(wordUid)).thenReturn(Optional.of(translation));

        boolean result = wordService.isTranslationCorrect(wordUid, translation, username);

        assertThat(result).isTrue();
        verify(userProgressService).updateProgress(wordUid, username, true);
    }

    @Test
    void isTranslationCorrect_ShouldReturnFalseAndCallProgress_WhenTranslationMismatch() {
        when(wordRepository.findTranslationByUid(wordUid)).thenReturn(Optional.of(translation));

        boolean result = wordService.isTranslationCorrect(wordUid, "wrong", username);

        assertThat(result).isFalse();
        verify(userProgressService).updateProgress(wordUid, username, false);
    }

    @Test
    void isTranslationCorrect_ShouldThrowEntityNotFoundException_WhenWordNotFound() {
        when(wordRepository.findTranslationByUid(wordUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.isTranslationCorrect(wordUid, translation, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Word mot found");
    }

    @Test
    void getClue_ShouldReturnClueAndAddTextClueType_WhenProgressExistsAndClueTypesNull() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(null);
        when(userProgressService.getUserProgressByWordUid(wordUid, username)).thenReturn(Optional.of(progress));
        when(wordRepository.findClueByUid(wordUid)).thenReturn(Optional.of(clue));

        String result = wordService.getClue(wordUid, username);

        assertThat(result).isEqualTo(clue);
        assertThat(progress.getUserClueTypes()).containsExactly(ClueType.TEXT);
        verify(userProgressService, never()).save(progress);
    }

    @Test
    void getClue_ShouldAddTextClueType_WhenUserClueTypesDoesNotContainText() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(new HashSet<>());
        when(userProgressService.getUserProgressByWordUid(wordUid, username)).thenReturn(Optional.of(progress));
        when(wordRepository.findClueByUid(wordUid)).thenReturn(Optional.of(clue));

        String result = wordService.getClue(wordUid, username);

        assertThat(result).isEqualTo(clue);
        assertThat(progress.getUserClueTypes()).containsExactly(ClueType.TEXT);
    }

    @Test
    void getClue_ShouldNotAddDuplicateTextClueType_WhenAlreadyContains() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(new HashSet<>(Set.of(ClueType.TEXT)));
        when(userProgressService.getUserProgressByWordUid(wordUid, username)).thenReturn(Optional.of(progress));
        when(wordRepository.findClueByUid(wordUid)).thenReturn(Optional.of(clue));

        String result = wordService.getClue(wordUid, username);

        assertThat(result).isEqualTo(clue);
        assertThat(progress.getUserClueTypes()).hasSize(1);
    }

    @Test
    void getClue_ShouldThrowEntityNotFoundException_WhenProgressNotFound() {
        when(userProgressService.getUserProgressByWordUid(wordUid, username)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.getClue(wordUid, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("ещё не начато изучение");
    }

    @Test
    void getClue_ShouldThrowEntityNotFoundException_WhenClueNotFound() {
        UserProgress progress = new UserProgress();
        progress.setUserClueTypes(new HashSet<>());
        when(userProgressService.getUserProgressByWordUid(wordUid, username)).thenReturn(Optional.of(progress));
        when(wordRepository.findClueByUid(wordUid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.getClue(wordUid, username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("подсказка для слова с uid");
    }

    @Test
    void getCurrentLearningWord_ShouldReturnWord_WhenProgressExists() {
        Word word = createTestWord();
        UserProgress progress = new UserProgress();
        progress.setWord(word);
        Set<UserProgressStatus> statuses = Set.of(UserProgressStatus.IN_PROGRESS, UserProgressStatus.SENT);

        when(userProgressRepository.findLastInLearningByUsernameAndStatuses(username, statuses))
                .thenReturn(Optional.of(progress));

        Optional<Word> result = wordService.getCurrentLearningWord(username);

        assertThat(result).isPresent().contains(word);
        verify(userProgressRepository).findLastInLearningByUsernameAndStatuses(username, statuses);
    }

    @Test
    void getCurrentLearningWord_ShouldThrowEntityNotFoundException_WhenNoProgress() {
        when(userProgressRepository.findLastInLearningByUsernameAndStatuses(anyString(), anySet()))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> wordService.getCurrentLearningWord(username))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("Не найден прогресс для пользователя");
    }
}