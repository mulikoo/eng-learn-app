package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Word;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordRepository extends org.springframework.data.repository.Repository<Word, Long> {

    Optional<Word> findByUid(UUID uid);
}


//SimpleJdbcRepository