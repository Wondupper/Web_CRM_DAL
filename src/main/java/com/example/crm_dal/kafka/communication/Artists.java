package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Artist;
import com.example.crm_dal.repositories.ArtistRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
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
public class Artists {
    private final ArtistRepository artistRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Artists(ArtistRepository artistRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.artistRepository = artistRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-artistsdal")
    public void sendArtists(String message) throws JsonProcessingException {
        List<Object> artists = new ArrayList<>(artistRepository.findAll());
        kafkaTemplate.send("get-artistsbl",mapper.writeValueAsString(artists));
    }

    @KafkaListener(topics = "get-artistdal")
    public void sendArtist(String id) throws JsonProcessingException {
        if(artistRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-artistbl", mapper.writeValueAsString(artistRepository.findById(Long.valueOf(id)).get()));
        }
    }

    @KafkaListener(topics = "save-artistdal")
    public void saveArtist(String artist) throws JsonProcessingException {
        artistRepository.save(mapper.readValue(artist,Artist.class));
    }
}
