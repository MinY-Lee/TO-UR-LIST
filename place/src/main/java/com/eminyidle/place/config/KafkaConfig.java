package com.eminyidle.place.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
@EnableKafka
public class KafkaConfig {

    // kafka와 연결하기 위한 서버
    @Value("${KAFKA_BOOTSTRAP_SERVERS}")
    private String KAFKA_BOOTSTRAP_SERVERS;

    // 서비스가 속한 그룹 -> 우리는 서버 이름
    @Value("${KAFKA_PLACE_CONSUMER_GROUP}")
    private String KAFKA_PLACE_CONSUMER_GROUP;

    // 카프카 producer 설정
    // 메시지를 보내는 것
    @Bean
    public ProducerFactory<String, String> producerFactory() {  // 프로듀서를 만들기 위한 것을 정의
        Map<String, Object> props = new HashMap<>();
        // kafka는 key가 없어도 보낼 수 있어... 직렬화 규칙을 동일하게 해 줘야해...
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        return new DefaultKafkaProducerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        Map<String, Object> config = new HashMap<>();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, KAFKA_BOOTSTRAP_SERVERS);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, KAFKA_PLACE_CONSUMER_GROUP);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        return new DefaultKafkaConsumerFactory<>(config, new StringDeserializer(),
                new StringDeserializer());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        // kafka 리스너 -> comsumer에 사용
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());

        return factory;
    }
}
