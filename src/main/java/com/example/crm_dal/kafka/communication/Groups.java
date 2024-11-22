package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Group;
import com.example.crm_dal.repositories.GroupRepository;
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
public class Groups {
    private final GroupRepository groupRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    public Groups(GroupRepository groupRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.groupRepository = groupRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-groupsdal")
    public void sendGroups(String message) throws JsonProcessingException {
        List<Object> groups = new ArrayList<>(groupRepository.findAll());
        kafkaTemplate.send("get-groupsbl", mapper.writeValueAsString(groups));
    }

    @KafkaListener(topics = "get-groupdal")
    public void sendGroup(String id) throws JsonProcessingException {
        if(groupRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-groupbl", mapper.writeValueAsString(groupRepository.findById(Long.valueOf(id)).get()));
        }
    }

    @KafkaListener(topics = "save-groupdal")
    public void saveGroup(String group) throws JsonProcessingException {
        groupRepository.save(mapper.readValue(group, Group.class));
    }
}
