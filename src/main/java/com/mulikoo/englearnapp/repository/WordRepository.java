package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Word;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordRepository extends CrudRepository<Word, Long> {

    Optional<Word> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByNameAndTranslation(String name, String translation);

}