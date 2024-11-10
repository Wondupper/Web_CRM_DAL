package com.example.crm_dal.kafka.communication;

import com.example.crm_dal.models.Artist;
import com.example.crm_dal.repositories.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableKafka
public class Artists {
    private final ArtistRepository artistRepository;

    private final KafkaTemplate<String, List<Artist>> listKafkaTemplate;

    private final KafkaTemplate<String, Artist> kafkaTemplate;

    @Autowired
    public Artists(ArtistRepository artistRepository, KafkaTemplate<String, List<Artist>> listKafkaTemplate, KafkaTemplate<String, Artist> kafkaTemplate) {
        this.artistRepository = artistRepository;
        this.listKafkaTemplate = listKafkaTemplate;
        this.kafkaTemplate = kafkaTemplate;
    }

    @KafkaListener(topics = "get-artists")
    public void sendArtists(String message){
        listKafkaTemplate.send("get-artists",artistRepository.findAll());
    }

    @KafkaListener(topics = "get-artist")
    public void sendArtist(Long id){
        if(artistRepository.findById(id).isPresent()) {
            kafkaTemplate.send("get-artist", artistRepository.findById(id).get());
        }
    }

    @KafkaListener(topics = "save-artist")
    public void saveArtist(Artist artist){
        artistRepository.save(artist);
    }
}
