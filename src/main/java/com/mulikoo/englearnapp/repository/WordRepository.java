package com.mulikoo.englearnapp.repository;

import com.mulikoo.englearnapp.entity.Word;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WordRepository extends CrudRepository<Word, Long>, PagingAndSortingRepository<Word, Long>{

    Optional<Word> findByUid(UUID uid);

    void deleteByUid(UUID uid);

    boolean existsByNameAndTranslation(String name, String translation);

}