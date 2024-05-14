package com.eminyidle.place.place.kafka;

import com.eminyidle.place.place.dto.KafkaMessage;
import com.eminyidle.place.place.dto.KafkaPlace;
import com.eminyidle.place.place.exception.ProduceMessageException;
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
public class PlaceKafkaProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    // 변경사항을 알릴 kafka topic
    @Value("${KAFKA_PLACE_ALERT_TOPIC}")
    private String KAFKA_PLACE_TOPIC;

    public void producePlaceKafkaMessage(String type, KafkaPlace kafkaPlace) {
        // CREATE: {tourId=f440c283-e33c, placeName=인스파이어아레나, tourDay=3, tourPlaceId=f98dld-djfh}
        // DELETE: {tourId=f440c283-e33c, placeName=인스파이어아레나, tourDay=3, tourPlaceId=null}
        produceKafkaMessage(KAFKA_PLACE_TOPIC, type, kafkaPlace);
    }

    // kafka message 생성
    public void produceKafkaMessage(String topic, String type, Object body) {
        log.info("produce kafka message" + type);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String message = objectMapper.writeValueAsString(KafkaMessage.builder()
                    .type(type)
                    .body(body)
                    .build()
            );
            kafkaTemplate.send(topic, message);
        } catch (JsonProcessingException e) {
            log.error("{}", e);
            throw new ProduceMessageException();
        }
    }


}
