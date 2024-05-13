package com.eminyidle.tour.adapter.in.messaging;

import com.eminyidle.tour.adapter.dto.messaging.KafkaMessage;
import com.eminyidle.tour.adapter.in.messaging.dto.UserNode;
import com.eminyidle.tour.application.dto.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TourKafkaListener {

    @KafkaListener(topics = "${KAFKA_USER_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeUserTopic(String kafkaMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            KafkaMessage message = objectMapper.readValue(kafkaMessage, KafkaMessage.class);
            if (message.getType().equals("UPDATE")) {
                //TODO- update UserNode
                UserNode user = (UserNode) message.getBody();
                //userId에 맞는 노드가 있다면
                User.builder()
                        .userId(user.getUserId())
                        .userName(user.getUserName())
                        .userNickname(user.getUserNickname())
                        .build();
                //이걸로 업데이트
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
