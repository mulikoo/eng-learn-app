package com.mulikoo.englearnapp.service;

import com.mulikoo.englearnapp.entity.Word;
import com.mulikoo.englearnapp.repository.WordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class WordService {
    private final WordRepository wordRepository;

   public Optional<Word> findByUid(@Nullable UUID uid) {
        if (uid == null) {
            log.warn("uid is null");
            return Optional.empty();
        }

        return wordRepository.findByUid(uid);
    }
}
