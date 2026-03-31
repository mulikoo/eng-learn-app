package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Category;
import com.mulikoo.englearnapp.entity.Word;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @EntityGraph(value = "Word.withCategory")
    Optional<Word> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByNameAndTranslation(String name, String translation);

    @EntityGraph(value = "Word.withCategory")
    @Query("SELECT w FROM Word w WHERE w.id NOT IN :excludedIds AND w.category = :category order by w.creationDate limit 1")
    Optional<Word> findNextByUserCategory(@Param("category") Category category,
                                          @Param("excludedIds") List<Long> excludedIds);

    @Override
    @EntityGraph(value = "Word.withCategory")
    Page<Word> findAll(Pageable pageable);
}