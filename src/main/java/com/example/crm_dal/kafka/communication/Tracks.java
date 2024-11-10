package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Track;
import com.example.crm_dal.repositories.TrackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableKafka
public class Tracks {
    private final TrackRepository trackRepository;

    private final KafkaTemplate<String, List<Track>> listKafkaTemplate;

    private final KafkaTemplate<String, Track> kafkaTemplate;

    @Autowired
    public Tracks(TrackRepository trackRepository, KafkaTemplate<String, List<Track>> listKafkaTemplate, KafkaTemplate<String, Track> kafkaTemplate) {
        this.trackRepository = trackRepository;
        this.listKafkaTemplate = listKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-tracks")
    public void sendTracks(String message){
        listKafkaTemplate.send("get-tracks",trackRepository.findAll());
    }

    @KafkaListener(topics = "get-track")
    public void sendTrack(String id){
        if(trackRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-track", trackRepository.findById(Long.valueOf(id)).get());
        }
    }

    @KafkaListener(topics = "save-track")
    public void saveTrack(Track track){
        trackRepository.save(track);
    }
}
