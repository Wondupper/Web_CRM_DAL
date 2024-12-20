package com.example.crm_dal.services;

import com.example.crm_dal.models.Genre;
import com.example.crm_dal.repositories.GenreRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@EnableKafka
public class Genres {
    private final GenreRepository genreRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Genres(GenreRepository genreRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.genreRepository = genreRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-genresdal")
    @Transactional
    public void sendGenres(String message) throws JsonProcessingException {
        List<Object> genres = new ArrayList<>(genreRepository.findAll());
        kafkaTemplate.send("get-genresbl",mapper.writeValueAsString(genres));
    }

    @KafkaListener(topics = "get-genredal")
    @Transactional
    public void sendGenre(String id) throws JsonProcessingException {
        if(genreRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-genrebl", mapper.writeValueAsString(genreRepository.findById(Long.valueOf(id)).get()));
        }else{
            kafkaTemplate.send("get-genrebl", null);
        }
    }

    @KafkaListener(topics = "save-genredal")
    @Transactional
    public void saveGenre(String message) throws JsonProcessingException {
        genreRepository.save(mapper.readValue(message, Genre.class));
    }
}
