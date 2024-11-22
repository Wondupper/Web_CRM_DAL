package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Genre;
import com.example.crm_dal.repositories.GenreRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
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
    public void sendGenres(String message) throws JsonProcessingException {
        List<Object> genres = new ArrayList<>(genreRepository.findAll());
        kafkaTemplate.send("get-genresbl",mapper.writeValueAsString(genres));
    }

    @KafkaListener(topics = "get-genredal")
    public void sendGenre(String id) throws JsonProcessingException {
        if(genreRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-genrebl", mapper.writeValueAsString(genreRepository.findById(Long.valueOf(id)).get()));
        }
    }

    @KafkaListener(topics = "save-genredal")
    public void saveGenre(String message) throws JsonProcessingException {
        genreRepository.save(mapper.readValue(message, Genre.class));
    }
}
