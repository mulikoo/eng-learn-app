package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.Phrase;
import com.mulikoo.englearnapp.entity.Word;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PhraseRepository extends JpaRepository<Phrase, Long> {

    @EntityGraph(value = "Phrase.withCategory")
    Optional<Phrase> findByUid(UUID uid);

    boolean existsByNameAndTranslation(String name, String translation);

    void deleteByUid(UUID uid);

    @Query("SELECT p.translation FROM Phrase p WHERE p.uid = :uid")
    Optional<String> findTranslationByUid(@Param("uid") UUID uid);

    @Query("SELECT p.clue FROM Phrase p WHERE p.uid = :uid")
    Optional<String> findClueByUid(UUID uid);

    @EntityGraph(value = "Phrase.withCategory")
    @Query("SELECT p FROM Phrase p WHERE p.id NOT IN :excludedIds AND p.category = :category order by p.creationDate limit 1")
    Optional<Phrase> findNextByUserCategory(@Param("category") Category category,
                                            @Param("excludedIds") List<Long> excludedIds);
}
