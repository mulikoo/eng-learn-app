package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordRepository extends JpaRepository<Word, Long>{

    Optional<Word> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByNameAndTranslation(String name, String translation);

}