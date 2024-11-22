package com.example.crm_dal.kafka.topics;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class GroupsTopics {
    @Bean
    public NewTopic getAllGroupsTopic() {
        return TopicBuilder.name("get-groupsdal")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic getGroupTopic() {
        return TopicBuilder.name("get-groupdal")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public NewTopic createGroupTopic() {
        return TopicBuilder.name("save-groupdal")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
