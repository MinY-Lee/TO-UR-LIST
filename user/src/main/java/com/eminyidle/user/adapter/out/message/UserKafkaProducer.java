package com.eminyidle.user.adapter.out.message;

import com.eminyidle.user.adapter.dto.message.KafkaMessage;
import com.eminyidle.user.application.port.out.UpdateUserPort;
import com.eminyidle.user.domain.User;
import com.eminyidle.user.exception.ProduceMessageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserKafkaProducer implements UpdateUserPort {
    @Value("${KAFKA_USER_ALERT_TOPIC}")
    private String KAFKA_USER_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(User user) {
        produceUserKafkaMessage("UPDATE", user);
    }

    public void produceUserKafkaMessage(String type, User user) {
        produceKafkaMessage(KAFKA_USER_TOPIC, type, user);
    }

    public void produceKafkaMessage(String topic, String type, Object body) {
        log.debug("produceTourKafkaMessage-" + type);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(KafkaMessage.builder()
                    .type(type)
                    .body(body)
                    .build()
            );
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message", e);
            throw new ProduceMessageException();
        }
    }

}
