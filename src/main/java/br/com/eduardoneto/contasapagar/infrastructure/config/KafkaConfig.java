package br.com.eduardoneto.contasapagar.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    public static final String CSV_IMPORT_TOPIC = "csv-import";

    @Bean
    public NewTopic csvImportTopic() {
        return TopicBuilder.name(CSV_IMPORT_TOPIC)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
