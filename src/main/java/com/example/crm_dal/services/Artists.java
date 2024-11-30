package com.example.crm_dal.services;

import com.example.crm_dal.models.Artist;
import com.example.crm_dal.models.Group;
import com.example.crm_dal.repositories.ArtistRepository;
import com.example.crm_dal.repositories.GroupRepository;
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
public class Artists {
    private final ArtistRepository artistRepository;

    private final GroupRepository groupRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public Artists(ArtistRepository artistRepository, GroupRepository groupRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.artistRepository = artistRepository;
        this.groupRepository = groupRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-artistsdal")
    @Transactional
    public void sendArtists(String message) throws JsonProcessingException {
        List<Object> artists = new ArrayList<>(artistRepository.findAll());
        kafkaTemplate.send("get-artistsbl",mapper.writeValueAsString(artists));
    }

    @KafkaListener(topics = "get-artistdal")
    @Transactional
    public void sendArtist(String id) throws JsonProcessingException {
        if(artistRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-artistbl", mapper.writeValueAsString(artistRepository.findById(Long.valueOf(id)).get()));
        }else{
            kafkaTemplate.send("get-artistbl", null);
        }
    }

    @KafkaListener(topics = "save-artistdal")
    @Transactional
    public void saveArtist(String artist) throws JsonProcessingException {
        String groupName = mapper.readTree(artist).path("group").asText();
        Group group = groupRepository.findByName(groupName);
        Artist artistObj = new Artist();
        artistObj.setName(mapper.readTree(artist).path("name").asText());
        artistObj.setGroup(group);
        artistObj.setSurname(mapper.readTree(artist).path("surname").asText());
        artistRepository.save(artistObj);
    }
}
