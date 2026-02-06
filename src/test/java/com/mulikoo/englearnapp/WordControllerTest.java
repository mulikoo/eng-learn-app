package com.mulikoo.englearnapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mulikoo.englearnapp.controller.WordController;
import com.mulikoo.englearnapp.dto.WordDto;
import com.mulikoo.englearnapp.mapper.WordMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(WordController.class)
public class WordControllerTest {

    private final static String URL = "/api/v1/words";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private WordMapper wordMapper;

    @DisplayName("получение слова по uid")
    @Test
    public void getWordCase1() throws Exception {
        mockMvc.perform(get("/api/v1/words/{uid}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))   //возвращаемое значение в виде Json
                .andExpect(jsonPath("$.name").value("apple"))
                .andExpect(jsonPath("$.translation").value("яблоко"));

    }

    @DisplayName("создание нового слова")
    @Test
    public void postWordCase1() throws Exception {

        var requestDto = new WordDto();
        requestDto.setName("apple");
        requestDto.setTranslation("яблоко");

        mockMvc.perform(
                        post(URL)
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("apple"))
                .andExpect(jsonPath("$.translation").value("яблоко"))
                .andExpect(jsonPath("$.uid").exists())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.modificationDate").exists());

    }


    @DisplayName("обновляем слово")
    @Test
    public void putWordCase1() throws Exception {
        var requestDto = new WordDto();
        requestDto.setName("apple");
        requestDto.setTranslation("яблоко");

        mockMvc.perform(
                        put("/api/v1/words/{uid}", UUID.randomUUID())
                                .content(objectMapper.writeValueAsString(requestDto))
                                .contentType(MediaType.APPLICATION_JSON)

                ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("apple"))
                .andExpect(jsonPath("$.translation").value("яблоко"))
                .andExpect(jsonPath("$.uid").exists())
                .andExpect(jsonPath("$.creationDate").exists())
                .andExpect(jsonPath("$.modificationDate").exists());
    }

    @DisplayName("удаление слова")
    @Test
    public void deleteWordCase1() throws Exception {
        mockMvc.perform(delete("/api/v1/words/{uid}", UUID.randomUUID()))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
