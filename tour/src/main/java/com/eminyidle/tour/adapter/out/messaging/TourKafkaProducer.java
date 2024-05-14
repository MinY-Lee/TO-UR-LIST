package com.eminyidle.tour.adapter.out.messaging;

import com.eminyidle.tour.adapter.dto.messaging.KafkaDetailMessage;
import com.eminyidle.tour.adapter.dto.messaging.KafkaMessage;
import com.eminyidle.tour.adapter.out.messaging.dto.PaymentMessage;
import com.eminyidle.tour.application.dto.Tour;
import com.eminyidle.tour.application.dto.TourMember;
import com.eminyidle.tour.exception.ProduceMessageException;
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
public class TourKafkaProducer {
    @Value("${KAFKA_TOUR_ALERT_TOPIC}")
    private String KAFKA_TOUR_TOPIC;

    @Value("${KAFKA_MEMBER_ALERT_TOPIC}")
    private String KAFKA_MEMBER_TOPIC;

    @Value("${KAFKA_PAYMENT_REQUEST_TOPIC}")
    private String KAFKA_PAYMENT_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceCreateTour(Tour tour, String hostId) {
        produceTourKafkaDetailMessage("CREATE", tour, hostId);
    }

    public void produceUpdateTourCity(Tour tour) {
        produceTourKafkaMessage("UPDATE_CITY", tour);
    }

    //TODO - place에 필요
    public void produceUpdateTourDate(Tour tour) {
        produceTourKafkaMessage("UPDATE_DATE", tour);
    }

    public void produceDeleteTour(Tour tour) {
        produceTourKafkaMessage("DELETE", tour);
    }

    public void produceCreateMember(String userId, String tourId) {
        produceTourMemberKafkaMessage("CREATE", TourMember.builder()
                .tourId(tourId)
                .userId(userId)
                .build());
    }

    public void produceDeleteMember(String userId, String tourId) {
        produceTourMemberKafkaMessage("DELETE", TourMember.builder()
                .tourId(tourId)
                .userId(userId)
                .build());
    }

    public void produceGhostToGuest(String tourId, String ghostId, String userId) {
        produceMessageWithKey(KAFKA_PAYMENT_TOPIC, tourId, PaymentMessage.builder()
                .tourId(tourId)
                .ghostId(ghostId)
                .userId(userId)
                .build());
    }


    //=====utils

    public void produceTourKafkaMessage(String type, Tour tour) {
        produceKafkaMessage(KAFKA_TOUR_TOPIC, type, tour);
    }

    public void produceTourKafkaDetailMessage(String type, Tour tour, String desc) {
        produceKafkaMessage(KAFKA_TOUR_TOPIC, type, tour, desc);
    }

    public void produceTourMemberKafkaMessage(String type, TourMember member) {
        produceKafkaMessage(KAFKA_MEMBER_TOPIC, type, member);
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

    public void produceKafkaMessage(String topic, String type, Object body, String desc) {
        log.debug("produceTourKafkaMessage-" + type);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonMessage = objectMapper.writeValueAsString(new KafkaDetailMessage(type, body, desc));
            kafkaTemplate.send(topic, jsonMessage);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize message", e);
            throw new ProduceMessageException();
        }
    }


    public void produceMessageWithKey(String topic, String key, Object message) {
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

}
