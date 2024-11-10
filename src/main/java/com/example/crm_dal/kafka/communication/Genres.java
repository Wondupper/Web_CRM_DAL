package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Genre;
import com.example.crm_dal.repositories.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableKafka
public class Genres {
    private final GenreRepository genreRepository;

    private final KafkaTemplate<String, List<Genre>> listKafkaTemplate;

    private final KafkaTemplate<String, Genre> kafkaTemplate;

    @Autowired
    public Genres(GenreRepository genreRepository, KafkaTemplate<String, List<Genre>> listKafkaTemplate, KafkaTemplate<String, Genre> kafkaTemplate) {
        this.genreRepository = genreRepository;
        this.listKafkaTemplate = listKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-genres")
    public void sendGenres(String message){
        listKafkaTemplate.send("get-genres",genreRepository.findAll());
    }

    @KafkaListener(topics = "get-genre")
    public void sendGenre(String id){
        if(genreRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-genre", genreRepository.findById(Long.valueOf(id)).get());
        }
    }

    @KafkaListener(topics = "save-genre")
    public void saveGenre(Genre genre){
        genreRepository.save(genre);
    }
}
