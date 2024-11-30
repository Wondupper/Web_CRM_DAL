package com.example.crm_dal.services;

import com.example.crm_dal.models.Group;
import com.example.crm_dal.models.Group;
import com.example.crm_dal.models.Track;
import com.example.crm_dal.repositories.GroupRepository;
import com.example.crm_dal.repositories.TrackRepository;
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
public class Tracks {
    private final TrackRepository trackRepository;

    private final GroupRepository groupRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;
    
    @Autowired
    public Tracks(TrackRepository trackRepository, GroupRepository groupRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.trackRepository = trackRepository;
        this.groupRepository = groupRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-tracksdal")
    @Transactional
    public void sendTracks(String message) throws JsonProcessingException {
        List<Object> tracks = new ArrayList<>(trackRepository.findAll());
        kafkaTemplate.send("get-tracksbl", mapper.writeValueAsString(tracks));
    }

    @KafkaListener(topics = "get-trackdal")
    @Transactional
    public void sendTrack(String id) throws JsonProcessingException {
        if(trackRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-trackbl", mapper.writeValueAsString(trackRepository.findById(Long.valueOf(id)).get()));
        }else{
            kafkaTemplate.send("get-trackbl", null);
        }
    }

    @KafkaListener(topics = "save-trackdal")
    @Transactional
    public void saveTrack(String track) throws JsonProcessingException {
        String groupName = mapper.readTree(track).path("group").asText();
        Group group = groupRepository.findByName(groupName);
        Track trackObj = new Track();
        trackObj.setName(mapper.readTree(track).path("name").asText());
        trackObj.setGroup(group);
        trackRepository.save(trackObj);
    }
}
