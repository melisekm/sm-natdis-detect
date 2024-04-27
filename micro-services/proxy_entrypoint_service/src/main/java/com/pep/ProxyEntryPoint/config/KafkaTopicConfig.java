package com.pep.ProxyEntryPoint.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfig {

    @Value(value = "${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    @Value("${prediction.topic}")
    private String predictionTopic;

    @Value("${ner.topic}")
    private String nerTopic;

    @Value("${link.topic}")
    private String linkTopic;

    @Value("${db.topic}")
    private String dbTopic;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapAddress);
        return new KafkaAdmin(configs);
    }


//    @Bean
//    public NewTopic predictionInitTopic() {
//        return new NewTopic("predictionInitTopic", 1, (short) 1);
//    }
//
//    @Bean
//    public NewTopic predictionNERTopic() {
//        return new NewTopic("predictionNERTopic", 1, (short) 1);
//    }
//
//    @Bean
//    public NewTopic basePredictionTopic() {
//        return new NewTopic("basePredictionTopic", 1, (short) 1);
//    }
//
//    @Bean
//    public NewTopic linkTopic() {
//        return new NewTopic("linkTopic", 1, (short) 1);
//    }
//
//    @Bean
//    public NewTopic finalPredictionTopic() {
//        return new NewTopic("finalPredictionTopic", 1, (short) 1);
//    }

    @Bean
    public NewTopic predictionTopic() {
        return new NewTopic(this.predictionTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic nerTopic() {
        return new NewTopic(this.nerTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic linkTopic() {
        return new NewTopic(this.linkTopic, 1, (short) 1);
    }

    @Bean
    public NewTopic dbTopic() {
        return new NewTopic(this.dbTopic, 1, (short) 1);
    }

}
