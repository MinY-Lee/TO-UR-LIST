package com.eminyidle.tour.adapter.out.messaging;

import com.eminyidle.tour.adapter.dto.messaging.KafkaMessage;
import com.eminyidle.tour.application.dto.Tour;
import com.eminyidle.tour.exception.ProduceMessageException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {
    @Value("${KAFKA_TOUR_ALERT_TOPIC}")
    private String KAFKA_TOUR_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceMessage(String topic, String value) {
        log.debug("produce String Message-" + topic);
        CompletableFuture send = kafkaTemplate.send(topic, value);
    }

    /**
     * @param type: CREATE | UPDATE | DELETE
     * @param tour
     */
    public void produceTourKafkaMessage(String type, Tour tour) {
        log.debug("produceTourKafkaMessage-" + type);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(KafkaMessage.builder()
                    .type(type)
                    .body(tour)
                    .build()
            );
            kafkaTemplate.send(KAFKA_TOUR_TOPIC, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message", e);
            throw new ProduceMessageException();
        }
    }


    public void produceMessage(String topic, Object message) {
        log.debug("produce Object Message-" + topic);
        log.debug(message.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message", e);
            throw new ProduceMessageException();
        }
    }

    public void produceMessage(String topic, String key, Object message) {
        log.debug("produceMessage with Key: " + key);
        log.debug(message.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            kafkaTemplate.send(topic, key, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message", e);
            throw new ProduceMessageException();
        }
    }


//    @KafkaListener(topics = "${KAFKA_PAYMENT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
//    public void receiveUUIDDataResult(ConsumerRecord<String, String> consumerRecord) {
//
//        if (consumerRecord.key().isBlank() || consumerRecord.value().isBlank()) {
//            throw new KafkaDataNotExistException("ID 데이터가 없습니다.");
//        }
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            Message message = mapper.readValue(consumerRecord.value(), Message.class);
//
//            log.debug("tourId: {}", consumerRecord.key());
//            log.debug("ghostId: {}", message.getGhostId());
//            log.debug("userId: {}", message.getUserId());
//
//            // 고스트 유저를 실제 유저로 변경
//            paymentService.updatePaymentUserId(consumerRecord.key(), message);
//        } catch (Exception e) {
//            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
//        }
//    }

}
