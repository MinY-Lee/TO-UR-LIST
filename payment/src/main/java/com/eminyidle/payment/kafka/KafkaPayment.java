package com.eminyidle.payment.kafka;

import com.eminyidle.payment.dto.Message;
import com.eminyidle.payment.exception.KafkaDataNotExistException;
import com.eminyidle.payment.service.PaymentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaPayment {

    private final PaymentService paymentService;

    @Value("${KAFKA_PAYMENT_TOPIC}")
    String paymentTopicName;

    @KafkaListener(topics = "${KAFKA_PAYMENT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    public void receiveUUIDDataResult(ConsumerRecord<String, String> consumerRecord) {

        if(consumerRecord.key().isBlank() || consumerRecord.value().isBlank()) {
            throw new KafkaDataNotExistException("ID 데이터가 없습니다.");
        }
        try {
            ObjectMapper mapper = new ObjectMapper();
            Message message = mapper.readValue(consumerRecord.value(), Message.class);

            log.debug("tourId: {}", consumerRecord.key());
            log.debug("ghostId: {}", message.getGhostId());
            log.debug("userId: {}", message.getUserId());

            // 고스트 유저를 실제 유저로 변경
            paymentService.updatePaymentUserId(consumerRecord.key(), message);
        }
        catch(Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }
}
