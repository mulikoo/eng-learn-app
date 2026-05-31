package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.dto.view.UserProgressView;
import com.mulikoo.englearnapp.dto.view.UserProgressViewPhrase;
import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.User;
import com.mulikoo.englearnapp.entity.UserProgress;
import com.mulikoo.englearnapp.enums.UserProgressStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUser(User user);

    @Query("""
            SELECT up.word.id as wordId, up.status as status FROM UserProgress up 
                        WHERE up.user = :user AND up.word.category = :category
            """)
    List<UserProgressView> findWordIdsByUser(@Param("user") User user, @Param("category") Category category);

    @Query("""
        SELECT up.phrase.id as phraseId, up.status as status FROM UserProgress up 
        WHERE up.user = :user AND up.phrase.category = :category
       """)
    List<UserProgressViewPhrase> findPhraseIdByUser(@Param("user") User user, @Param("category") Category category);

    @Query("SELECT up FROM UserProgress up WHERE up.word.uid = :wordUid AND up.user.username = :username")
    Optional<UserProgress> findByWordUidAndUsername(@Param("wordUid") UUID wordUid, @Param("username") String username);

    @Query("SELECT up FROM UserProgress up WHERE up.phrase.uid = :phraseUid AND up.user.username = :username")
    Optional<UserProgress> findByPhraseUidAndUsername(@Param("phraseUid") UUID wordUid, @Param("username") String username);

    @EntityGraph(value = "UserProgress.withWord")
    @Query("SELECT up FROM UserProgress up WHERE up.user.username = :username " +
            "AND up.status in :statuses ORDER BY up.modificationDate desc LIMIT 1")
    Optional<UserProgress> findLastInLearningByUsernameAndStatuses(@Param("username") String username,
                                                                   @Param("statuses") Set<UserProgressStatus> status);
}