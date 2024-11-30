package com.example.crm_dal.services;

import com.example.crm_dal.models.Genre;
import com.example.crm_dal.models.Group;
import com.example.crm_dal.models.Track;
import com.example.crm_dal.repositories.ScheduleRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableKafka
public class Schedule {
    private final ScheduleRepository scheduleRepository;

    private final TrackRepository trackRepository;

    private ObjectMapper mapper = new ObjectMapper();

    private final KafkaTemplate<String, String > kafkaTemplate;

    @Autowired
    public Schedule(ScheduleRepository scheduleRepository, TrackRepository trackRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.scheduleRepository = scheduleRepository;
        this.trackRepository = trackRepository;
        this.kafkaTemplate = kafkaTemplate;
    }


    @KafkaListener(topics = "get-allScheduledal")
    @Transactional
    public void sendAllSchedule(String message) throws JsonProcessingException {
        List<Object> allSchedule = new ArrayList<>(scheduleRepository.findAll());
        kafkaTemplate.send("get-allSchedulebl", mapper.writeValueAsString(allSchedule));
    }

    @KafkaListener(topics = "get-scheduledal")
    @Transactional
    public void sendSchedule(String id) throws JsonProcessingException {
        if(scheduleRepository.findById(Long.valueOf(id)).isPresent()) {
            kafkaTemplate.send("get-schedulebl", mapper.writeValueAsString(scheduleRepository.findById(Long.valueOf(id)).get()));
        }else{
            kafkaTemplate.send("get-schedulebl", null);
        }
    }

    @KafkaListener(topics = "save-scheduledal")
    @Transactional
    public void saveSchedule(String schedule) throws JsonProcessingException {
        String trackName = mapper.readTree(schedule).path("track").asText();
        Track track = trackRepository.findByName(trackName);
        com.example.crm_dal.models.Schedule scheduleObj = new com.example.crm_dal.models.Schedule();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime time = LocalDateTime.parse(mapper.readTree(schedule).path("time").asText(), formatter);
        scheduleObj.setTime(time);
        scheduleObj.setTrack(track);
        scheduleRepository.save(scheduleObj);
    }
}
