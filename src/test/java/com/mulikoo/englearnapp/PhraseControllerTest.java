package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.PhraseController;
import com.mulikoo.englearnapp.dto.PhraseDto;
import com.mulikoo.englearnapp.entity.Phrase;
import com.mulikoo.englearnapp.mapper.PhraseMapper;
import com.mulikoo.englearnapp.service.PhraseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class PhraseControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PhraseService phraseService;

    @Mock
    private PhraseMapper phraseMapper;

    @InjectMocks
    private PhraseController phraseController;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private static final UUID TEST_UID = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
    private static final UUID CATEGORY_UID = UUID.fromString("123e4567-e89b-12d3-a456-426614174001");
    private static final String TEST_NAME = "Break a leg";
    private static final String TEST_TRANSLATION = "Ни пуха ни пера";
    private static final String TEST_CLUE = "пожелание удачи перед выступлением";
    private static final String UPDATED_TRANSLATION = "Удачи";

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(phraseController).build();
    }

    private Phrase createTestPhrase() {
        Phrase phrase = new Phrase();
        phrase.setUid(TEST_UID);
        phrase.setName(TEST_NAME);
        phrase.setTranslation(TEST_TRANSLATION);
        return phrase;
    }

    private PhraseDto createResponseDto() {
        PhraseDto dto = new PhraseDto();
        dto.setUid(TEST_UID);
        dto.setName(TEST_NAME);
        dto.setTranslation(TEST_TRANSLATION);
        dto.setClue(TEST_CLUE);
        dto.setCategoryUid(CATEGORY_UID);
        return dto;
    }

    private PhraseDto createRequestDto() {
        PhraseDto dto = new PhraseDto();
        dto.setName(TEST_NAME);
        dto.setTranslation(TEST_TRANSLATION);
        dto.setClue(TEST_CLUE);
        dto.setCategoryUid(CATEGORY_UID);
        return dto;
    }

    private PhraseDto createUpdateRequestDto() {
        PhraseDto dto = new PhraseDto();
        dto.setName(TEST_NAME);
        dto.setTranslation(UPDATED_TRANSLATION);
        dto.setClue(TEST_CLUE);
        dto.setCategoryUid(CATEGORY_UID);
        return dto;
    }

    private PhraseDto createUpdateResponseDto() {
        PhraseDto dto = new PhraseDto();
        dto.setUid(TEST_UID);
        dto.setName(TEST_NAME);
        dto.setTranslation(UPDATED_TRANSLATION);
        dto.setClue(TEST_CLUE);
        dto.setCategoryUid(CATEGORY_UID);
        return dto;
    }

    @Test
    void getPhrase_shouldReturnPhraseDto_whenPhraseExists() throws Exception {
        Phrase phrase = createTestPhrase();
        PhraseDto dto = createResponseDto();

        when(phraseService.findByUid(TEST_UID)).thenReturn(Optional.of(phrase));
        when(phraseMapper.toDto(phrase)).thenReturn(dto);

        mockMvc.perform(get("/api/v1/phrase/{uid}", TEST_UID))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(TEST_UID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.translation").value(TEST_TRANSLATION));
    }

    @Test
    void getPhrase_shouldReturnNotFound_whenPhraseDoesNotExist() throws Exception {
        when(phraseService.findByUid(TEST_UID)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/phrase/{uid}", TEST_UID))
                .andExpect(status().isNotFound());
    }

    @Test
    void createPhrase_shouldReturnCreatedPhrase() throws Exception {
        Phrase savedPhrase = createTestPhrase();
        PhraseDto responseDto = createResponseDto();

        when(phraseService.create(any(PhraseDto.class))).thenReturn(Optional.of(savedPhrase));
        when(phraseMapper.toDto(savedPhrase)).thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(createRequestDto());

        mockMvc.perform(post("/api/v1/phrase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uid").value(TEST_UID.toString()))
                .andExpect(jsonPath("$.name").value(TEST_NAME))
                .andExpect(jsonPath("$.translation").value(TEST_TRANSLATION));
    }

    @Test
    void createPhrase_shouldReturnBadRequest_whenInvalidData() throws Exception {
        String invalidJson = "{\"name\":\"\",\"translation\":\"\",\"clue\":\"\",\"categoryUid\":\"\"}";

        mockMvc.perform(post("/api/v1/phrase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatePhrase_shouldReturnUpdatedPhrase() throws Exception {
        Phrase updatedPhrase = createTestPhrase();
        updatedPhrase.setTranslation(UPDATED_TRANSLATION);
        PhraseDto responseDto = createUpdateResponseDto();

        when(phraseService.update(eq(TEST_UID), any(PhraseDto.class))).thenReturn(Optional.of(updatedPhrase));
        when(phraseMapper.toDto(updatedPhrase)).thenReturn(responseDto);

        String json = objectMapper.writeValueAsString(createUpdateRequestDto());

        mockMvc.perform(put("/api/v1/phrase/{uid}", TEST_UID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.translation").value(UPDATED_TRANSLATION));
    }

    @Test
    void updatePhrase_shouldReturnNotFound_whenPhraseDoesNotExist() throws Exception {
        when(phraseService.update(eq(TEST_UID), any(PhraseDto.class))).thenReturn(Optional.empty());

        String json = objectMapper.writeValueAsString(createUpdateRequestDto());

        mockMvc.perform(put("/api/v1/phrase/{uid}", TEST_UID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound());
    }

    @Test
    void deletePhrase_shouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/v1/phrase/{uid}", TEST_UID))
                .andExpect(status().isNoContent());
    }
}