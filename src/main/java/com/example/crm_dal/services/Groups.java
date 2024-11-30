package com.example.crm_dal.services;

import com.example.crm_dal.models.Artist;
import com.example.crm_dal.models.Genre;
import com.example.crm_dal.models.Group;
import com.example.crm_dal.repositories.GenreRepository;
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
public class Groups {
    private final GroupRepository groupRepository;

    private final GenreRepository genreRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public Groups(GroupRepository groupRepository, GenreRepository genreRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.groupRepository = groupRepository;
        this.genreRepository = genreRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-groupsdal")
    @Transactional
    public void sendGroups(String message) throws JsonProcessingException {
        List<Object> groups = new ArrayList<>(groupRepository.findAll());
        kafkaTemplate.send("get-groupsbl", mapper.writeValueAsString(groups));
    }

    @KafkaListener(topics = "get-groupdal")
    @Transactional
    public void sendGroup(String id) throws JsonProcessingException {
        if(groupRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-groupbl", mapper.writeValueAsString(groupRepository.findById(Long.valueOf(id)).get()));
        }else{
            kafkaTemplate.send("get-groupbl", null);
        }
    }

    @KafkaListener(topics = "save-groupdal")
    @Transactional
    public void saveGroup(String group) throws JsonProcessingException {
        String genreName = mapper.readTree(group).path("genre").asText();
        Genre genre = genreRepository.findByName(genreName);
        Group groupObj = new Group();
        groupObj.setName(mapper.readTree(group).path("name").asText());
        groupObj.setGenre(genre);
        groupRepository.save(groupObj);
    }
}
