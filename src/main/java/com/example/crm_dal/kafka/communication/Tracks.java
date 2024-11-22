package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Track;
import com.example.crm_dal.repositories.TrackRepository;
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
public class Tracks {
    private final TrackRepository trackRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Tracks(TrackRepository trackRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.trackRepository = trackRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-tracksdal")
    public void sendTracks(String message) throws JsonProcessingException {
        List<Object> tracks = new ArrayList<>(trackRepository.findAll());
        kafkaTemplate.send("get-tracksbl", mapper.writeValueAsString(tracks));
    }

    @KafkaListener(topics = "get-trackdal")
    public void sendTrack(String id) throws JsonProcessingException {
        if(trackRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-trackbl", mapper.writeValueAsString(trackRepository.findById(Long.valueOf(id)).get()));
        }
    }

    @KafkaListener(topics = "save-trackdal")
    public void saveTrack(String track) throws JsonProcessingException {
        trackRepository.save(mapper.readValue(track, Track.class));
    }
}
