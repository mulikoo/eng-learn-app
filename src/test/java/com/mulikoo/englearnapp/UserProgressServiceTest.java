package com.mulikoo.englearnapp;

import com.mulikoo.englearnapp.dto.view.UserProgressView;
import com.mulikoo.englearnapp.dto.view.UserProgressViewPhrase;
import com.mulikoo.englearnapp.entity.*;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import com.mulikoo.englearnapp.exceptions.AttemptCounterException;
import com.mulikoo.englearnapp.exceptions.EntityNotFoundException;
import com.mulikoo.englearnapp.repository.UserProgressRepository;
import com.mulikoo.englearnapp.service.UserProgressService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProgressServiceTest {

    @Mock
    private UserProgressRepository userProgressRepository;

    @InjectMocks
    private UserProgressService userProgressService;

    private User testUser;
    private Category testCategory;
    private Word testWord;
    private Phrase testPhrase;
    private UserProgress testProgress;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUid(UUID.randomUUID());
        testUser.setUsername("testUser");

        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Test Category");

        testWord = new Word();
        testWord.setUid(UUID.randomUUID());
        testWord.setName("apple");
        testWord.setTranslation("яблоко");

        testPhrase = new Phrase();
        testPhrase.setUid(UUID.randomUUID());
        testPhrase.setName("How are you?");
        testPhrase.setTranslation("Как дела?");

        testProgress = new UserProgress();
        testProgress.setId(1L);
        testProgress.setUid(UUID.randomUUID());
        testProgress.setUser(testUser);
        testProgress.setWord(testWord);
        testProgress.setStatus(UserProgressStatus.SENT);
        testProgress.setAttemptCounter(0);
    }

    @Test
    void findAll_shouldReturnListOfUserProgress() {
        List<UserProgress> expectedList = List.of(testProgress);
        when(userProgressRepository.findAll()).thenReturn(expectedList);

        List<UserProgress> result = userProgressService.findAll();

        assertThat(result).hasSize(1).containsExactly(testProgress);
        verify(userProgressRepository).findAll();
    }

    @Test
    void save_shouldReturnSavedProgress() {
        when(userProgressRepository.save(testProgress)).thenReturn(testProgress);

        UserProgress saved = userProgressService.save(testProgress);

        assertThat(saved).isEqualTo(testProgress);
        verify(userProgressRepository).save(testProgress);
    }

    @Test
    void deleteById_whenIdIsNull_shouldDoNothing() {
        userProgressService.deleteById(null);
        verify(userProgressRepository, never()).deleteById(any());
    }

    @Test
    void deleteById_whenIdIsNotNull_shouldCallRepositoryDelete() {
        Long id = 1L;
        userProgressService.deleteById(id);
        verify(userProgressRepository).deleteById(id);
    }

    @Test
    void findWordIdsByUser_shouldReturnViews() {
        UserProgressView view = mock(UserProgressView.class);
        List<UserProgressView> expected = List.of(view);
        when(userProgressRepository.findWordIdsByUser(testUser, testCategory)).thenReturn(expected);

        List<UserProgressView> result = userProgressService.findWordIdsByUser(testUser, testCategory);

        assertThat(result).isSameAs(expected);
        verify(userProgressRepository).findWordIdsByUser(testUser, testCategory);
    }

    @Test
    void findPhraseIdByUser_shouldReturnViews() {
        UserProgressViewPhrase view = mock(UserProgressViewPhrase.class);
        List<UserProgressViewPhrase> expected = List.of(view);
        when(userProgressRepository.findPhraseIdByUser(testUser, testCategory)).thenReturn(expected);

        List<UserProgressViewPhrase> result = userProgressService.findPhraseIdByUser(testUser, testCategory);

        assertThat(result).isSameAs(expected);
        verify(userProgressRepository).findPhraseIdByUser(testUser, testCategory);
    }

    @Test
    void registerUserProgress_shouldCreateAndSaveNewProgress() {
        when(userProgressRepository.save(any(UserProgress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProgress result = userProgressService.registerUserProgress(testUser, testWord);

        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getWord()).isEqualTo(testWord);
        assertThat(result.getStatus()).isEqualTo(UserProgressStatus.SENT);
        assertThat(result.getUid()).isNotNull();
        verify(userProgressRepository).save(any(UserProgress.class));
    }

    @Test
    void registerUserProgressForPhrase_shouldCreateAndSaveNewProgress() {
        when(userProgressRepository.save(any(UserProgress.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserProgress result = userProgressService.registerUserProgressForPhrase(testUser, testPhrase);

        assertThat(result.getUser()).isEqualTo(testUser);
        assertThat(result.getPhrase()).isEqualTo(testPhrase);
        assertThat(result.getStatus()).isEqualTo(UserProgressStatus.SENT);
        assertThat(result.getUid()).isNotNull();
        verify(userProgressRepository).save(any(UserProgress.class));
    }

    @Test
    void updateProgress_whenCorrectAnswer_shouldSetStatusLearned() {
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        userProgressService.updateProgress(wordUid, username, true);

        assertThat(testProgress.getStatus()).isEqualTo(UserProgressStatus.LEARNED);
        assertThat(testProgress.getAttemptCounter()).isEqualTo(1);
        verify(userProgressRepository).save(testProgress);
    }

    @Test
    void updateProgress_whenIncorrectAnswer_shouldSetStatusInProgress() {
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        userProgressService.updateProgress(wordUid, username, false);

        assertThat(testProgress.getStatus()).isEqualTo(UserProgressStatus.IN_PROGRESS);
        assertThat(testProgress.getAttemptCounter()).isEqualTo(1);
        verify(userProgressRepository).save(testProgress);
    }

    @Test
    void updateProgress_whenProgressNotFound_shouldThrowEntityNotFoundException() {
        UUID wordUid = UUID.randomUUID();
        String username = "unknown";
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProgressService.updateProgress(wordUid, username, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("не найден прогресс по слову с uid: " + wordUid)
                .hasMessageContaining(username);

        verify(userProgressRepository, never()).save(any());
    }

    @Test
    void updateProgress_whenAttemptCounterExceedsMax_shouldThrowAttemptCounterExceptionAndSetFailed() {
        testProgress.setAttemptCounter(3); // MAX_ATTEMPT_COUNTER = 3
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        assertThatThrownBy(() -> userProgressService.updateProgress(wordUid, username, true))
                .isInstanceOf(AttemptCounterException.class)
                .hasMessageContaining("Превышено число попыток")
                .hasMessageContaining(wordUid.toString())
                .hasMessageContaining(username);

        assertThat(testProgress.getStatus()).isEqualTo(UserProgressStatus.FAILED);
        assertThat(testProgress.getAttemptCounter()).isEqualTo(3);
        verify(userProgressRepository).save(testProgress);
    }

    @Test
    void updateProgressPhrase_whenCorrectAnswer_shouldSetStatusLearned() {
        UUID phraseUid = testPhrase.getUid();
        String username = testUser.getUsername();
        UserProgress phraseProgress = new UserProgress();
        phraseProgress.setUid(UUID.randomUUID());
        phraseProgress.setUser(testUser);
        phraseProgress.setPhrase(testPhrase);
        phraseProgress.setStatus(UserProgressStatus.SENT);
        phraseProgress.setAttemptCounter(0);

        when(userProgressRepository.findByPhraseUidAndUsername(phraseUid, username))
                .thenReturn(Optional.of(phraseProgress));

        userProgressService.updateProgressPhrase(phraseUid, username, true);

        assertThat(phraseProgress.getStatus()).isEqualTo(UserProgressStatus.LEARNED);
        assertThat(phraseProgress.getAttemptCounter()).isEqualTo(1);
        verify(userProgressRepository).save(phraseProgress);
    }

    @Test
    void updateProgressPhrase_whenProgressNotFound_shouldThrowEntityNotFoundException() {
        UUID phraseUid = UUID.randomUUID();
        String username = "unknown";
        when(userProgressRepository.findByPhraseUidAndUsername(phraseUid, username))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> userProgressService.updateProgressPhrase(phraseUid, username, true))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("не найден прогресс по фразе с uid: " + phraseUid);

        verify(userProgressRepository, never()).save(any());
    }

    @Test
    void getUserProgressByWordUid_shouldReturnOptional() {
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        Optional<UserProgress> result = userProgressService.getUserProgressByWordUid(wordUid, username);

        assertThat(result).isPresent().contains(testProgress);
        verify(userProgressRepository).findByWordUidAndUsername(wordUid, username);
    }

    @Test
    void getUserProgressByPhraseUid_shouldReturnOptional() {
        UUID phraseUid = testPhrase.getUid();
        String username = testUser.getUsername();
        UserProgress phraseProgress = new UserProgress();
        phraseProgress.setPhrase(testPhrase);
        when(userProgressRepository.findByPhraseUidAndUsername(phraseUid, username))
                .thenReturn(Optional.of(phraseProgress));

        Optional<UserProgress> result = userProgressService.getUserProgressByPhraseUid(phraseUid, username);

        assertThat(result).isPresent().contains(phraseProgress);
        verify(userProgressRepository).findByPhraseUidAndUsername(phraseUid, username);
    }

    @Test
    void updateProgress_shouldCallRepositorySaveExactlyOnce() {
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        userProgressService.updateProgress(wordUid, username, true);

        verify(userProgressRepository, times(1)).save(testProgress);
    }

    @Test
    void updateProgress_whenExceptionOccurs_shouldNotCallSaveForFurtherUpdates() {
        testProgress.setAttemptCounter(3);
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        assertThatThrownBy(() -> userProgressService.updateProgress(wordUid, username, true))
                .isInstanceOf(AttemptCounterException.class);
        verify(userProgressRepository, times(1)).save(testProgress);
    }

    @Test
    void deleteById_whenIdIsNull_shouldNeverCallRepositoryDelete() {
        userProgressService.deleteById(null);
        verify(userProgressRepository, never()).deleteById(any());
    }

    @Test
    void registerUserProgress_shouldPassCorrectDataToRepository() {
        ArgumentCaptor<UserProgress> captor = ArgumentCaptor.forClass(UserProgress.class);
        when(userProgressRepository.save(captor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        UserProgress result = userProgressService.registerUserProgress(testUser, testWord);

        assertThat(result).isNotNull();
        UserProgress captured = captor.getValue();
        assertThat(captured.getUser()).isEqualTo(testUser);
        assertThat(captured.getWord()).isEqualTo(testWord);
        assertThat(captured.getStatus()).isEqualTo(UserProgressStatus.SENT);
        assertThat(captured.getUid()).isNotNull();
    }

    @ParameterizedTest
    @CsvSource({
            "0, true, LEARNED, 1",
            "0, false, IN_PROGRESS, 1",
            "1, true, LEARNED, 2",
            "1, false, IN_PROGRESS, 2",
            "2, true, LEARNED, 3",
            "2, false, IN_PROGRESS, 3"
    })
    void updateProgress_withDifferentAttemptsAndAnswers_shouldSetCorrectStatusAndIncrementCounter(
            int initialAttempts, boolean isCorrect, UserProgressStatus expectedStatus, int expectedCounter) {
        testProgress.setAttemptCounter(initialAttempts);
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        userProgressService.updateProgress(wordUid, username, isCorrect);

        assertThat(testProgress.getStatus()).isEqualTo(expectedStatus);
        assertThat(testProgress.getAttemptCounter()).isEqualTo(expectedCounter);
        verify(userProgressRepository).save(testProgress);
    }

    @ParameterizedTest
    @CsvSource({
            "3, true",
            "3, false",
            "4, true",
            "5, false"
    })
    void updateProgress_whenAttemptCounterAlreadyAtMax_shouldThrowExceptionAndSetFailed(
            int initialAttempts, boolean isCorrect) {
        testProgress.setAttemptCounter(initialAttempts);
        UUID wordUid = testWord.getUid();
        String username = testUser.getUsername();
        when(userProgressRepository.findByWordUidAndUsername(wordUid, username))
                .thenReturn(Optional.of(testProgress));

        assertThatThrownBy(() -> userProgressService.updateProgress(wordUid, username, isCorrect))
                .isInstanceOf(AttemptCounterException.class)
                .hasMessageContaining("Превышено число попыток");

        assertThat(testProgress.getStatus()).isEqualTo(UserProgressStatus.FAILED);
        assertThat(testProgress.getAttemptCounter()).isEqualTo(initialAttempts);
        verify(userProgressRepository).save(testProgress);
    }
}