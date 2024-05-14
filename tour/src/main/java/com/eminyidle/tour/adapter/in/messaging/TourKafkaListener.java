package com.eminyidle.tour.adapter.in.messaging;

import com.eminyidle.tour.adapter.dto.messaging.KafkaMessage;
import com.eminyidle.tour.adapter.in.messaging.dto.UserKafkaMessage;
import com.eminyidle.tour.adapter.in.messaging.dto.UserNode;
import com.eminyidle.tour.application.dto.User;
import com.eminyidle.tour.application.port.UserUpdateUsecase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourKafkaListener {

    private final UserUpdateUsecase userService;

    @KafkaListener(topics = "${KAFKA_USER_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeUserTopic(String kafkaMessage) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            UserKafkaMessage message = objectMapper.readValue(kafkaMessage, UserKafkaMessage.class);
            if (message.getType().equals("UPDATE")) {
                UserNode user = message.getBody();
                log.debug("변경사항: "+user.toString());
                userService.updateUser(
                        User.builder()
                                .userId(user.getUserId())
                                .userName(user.getUserName())
                                .userNickname(user.getUserNickname())
                                .build()
                );
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
