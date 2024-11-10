package com.example.crm_dal.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class TracksTopics {
    @Bean
    public NewTopic getAllTracksTopic() {
        return TopicBuilder.name("get-tracks")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getTrackTopic() {
        return TopicBuilder.name("get-track")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic createTrackTopic() {
        return TopicBuilder.name("save-track")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
