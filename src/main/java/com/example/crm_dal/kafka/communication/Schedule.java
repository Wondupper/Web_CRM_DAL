package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.repositories.ScheduleRepository;
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
public class Schedule {
    private final ScheduleRepository scheduleRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String > kafkaTemplate;

    @Autowired
    public Schedule(ScheduleRepository scheduleRepository, KafkaTemplate<String,String> kafkaTemplate) {
        this.scheduleRepository = scheduleRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-allScheduledal")
    public void sendAllSchedule(String message) throws JsonProcessingException {
        List<Object> allSchedule = new ArrayList<>(scheduleRepository.findAll());
        kafkaTemplate.send("get-allSchedulebl", mapper.writeValueAsString(allSchedule));
    }

    @KafkaListener(topics = "get-scheduledal")
    public void sendSchedule(String id) throws JsonProcessingException {
        if(scheduleRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-schedulebl", mapper.writeValueAsString(scheduleRepository.findById(Long.valueOf(id)).get()));
        }
    }

    @KafkaListener(topics = "save-scheduledal")
    public void saveSchedule(String schedule) throws JsonProcessingException {
        scheduleRepository.save(mapper.readValue(schedule, com.example.crm_dal.models.Schedule.class));
    }
}
