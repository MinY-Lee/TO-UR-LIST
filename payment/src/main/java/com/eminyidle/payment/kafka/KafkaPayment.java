package com.eminyidle.payment.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaPayment {
    @Value("${KAFKA_PAYMENT_TOPIC}")
    String paymentTopicName;

    @KafkaListener(topics = "${KAFKA_PAYMENT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    public void receiveUUIDDataResult(ConsumerRecord<String, String> consumerRecord) {

        String ghostId = consumerRecord.key();   // ghostId
        String userId = consumerRecord.value();  // userId

        log.debug("GhostId: {}", ghostId);
        log.debug("UserId: {}", userId);
    }

}
