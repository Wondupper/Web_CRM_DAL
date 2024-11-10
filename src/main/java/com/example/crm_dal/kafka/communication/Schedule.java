package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.repositories.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableKafka
public class Schedule {
    private final ScheduleRepository scheduleRepository;

    private final KafkaTemplate<String, List<com.example.crm_dal.models.Schedule>> listKafkaTemplate;

    private final KafkaTemplate<String, com.example.crm_dal.models.Schedule> kafkaTemplate;

    @Autowired
    public Schedule(ScheduleRepository scheduleRepository, KafkaTemplate<String, List<com.example.crm_dal.models.Schedule>> listKafkaTemplate, KafkaTemplate<String, com.example.crm_dal.models.Schedule> kafkaTemplate) {
        this.scheduleRepository = scheduleRepository;
        this.listKafkaTemplate = listKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-allSchedule")
    public void sendAllSchedule(String message){
        listKafkaTemplate.send("get-allSchedule",scheduleRepository.findAll());
    }

    @KafkaListener(topics = "get-schedule")
    public void sendSchedule(String id){
        if(scheduleRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-schedule", scheduleRepository.findById(Long.valueOf(id)).get());
        }
    }

    @KafkaListener(topics = "save-schedule")
    public void saveSchedule(com.example.crm_dal.models.Schedule schedule){
        scheduleRepository.save(schedule);
    }
}
