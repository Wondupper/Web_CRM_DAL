package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Group;
import com.example.crm_dal.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableKafka
public class Groups {
    private final GroupRepository groupRepository;

    private final KafkaTemplate<String, List<Group>> listKafkaTemplate;

    private final KafkaTemplate<String, Group> kafkaTemplate;

    @Autowired
    public Groups(GroupRepository groupRepository, KafkaTemplate<String, List<Group>> listKafkaTemplate, KafkaTemplate<String, Group> kafkaTemplate) {
        this.groupRepository = groupRepository;
        this.listKafkaTemplate = listKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-groups")
    public void sendGroups(String message){
        listKafkaTemplate.send("get-groups",groupRepository.findAll());
    }

    @KafkaListener(topics = "get-group")
    public void sendGroup(String id){
        if(groupRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-group", groupRepository.findById(Long.valueOf(id)).get());
        }
    }

    @KafkaListener(topics = "save-group")
    public void saveGroup(Group group){
        groupRepository.save(group);
    }
}
