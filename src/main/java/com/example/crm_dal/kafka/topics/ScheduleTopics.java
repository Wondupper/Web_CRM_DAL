package com.example.crm_dal.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class ScheduleTopics {
    @Bean
    public NewTopic getAllScheduleTopic() {
        return TopicBuilder.name("get-allScheduledal")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getScheduleTopic() {
        return TopicBuilder.name("get-scheduledal")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic createScheduleTopic() {
        return TopicBuilder.name("save-scheduledal")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
