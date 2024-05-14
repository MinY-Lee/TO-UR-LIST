package com.eminyidle.place.place.kafka;

import com.eminyidle.place.place.dto.KafkaMessage;
import com.eminyidle.place.place.service.ActivityService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaActivity {

    private final ActivityService activityService;

    @Value("${KAFKA_ACTIVITY_ALERT_TOPIC}")
    String KAFKA_ACTIVITY_ALERT_TOPIC;

    @KafkaListener(topics = "${KAFKA_ACTIVITY_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    public void receiveActivityAlert(ConsumerRecord<String, String> consumerRecord) {
        log.info("activity kafka 시작");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            KafkaMessage message = objectMapper.readValue(consumerRecord.value(), KafkaMessage.class);
            Map<String, Object> placeMap = (Map<String, Object>) message.getBody();
            log.info(message.getType());
            log.info("placeMap 확인: " + placeMap.toString());
        } catch (Exception e) {
            log.error("{}", e);
        }
    }
}
