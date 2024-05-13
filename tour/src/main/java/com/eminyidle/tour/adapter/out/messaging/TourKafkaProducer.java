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
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Slf4j
@Component
@RequiredArgsConstructor
public class TourKafkaProducer {
    @Value("${KAFKA_TOUR_ALERT_TOPIC}")
    private String KAFKA_TOUR_TOPIC;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void produceCreateTour(Tour tour){
        produceTourKafkaMessage("CREATE",tour);
    }

    public void produceUpdateTourCity(Tour tour){
        produceTourKafkaMessage("UPDATE",tour);
    }

    public void produceDeleteTour(Tour tour){
        produceTourKafkaMessage("DELETE",tour);
    }

    public void produceCreateMember(String userId,String tourId){

    }

    public void produceDeleteMember(String userId,String tourId){

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

}
