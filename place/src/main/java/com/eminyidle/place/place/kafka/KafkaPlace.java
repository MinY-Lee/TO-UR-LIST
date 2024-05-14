package com.eminyidle.place.place.kafka;

import com.eminyidle.place.place.dto.KafkaMessage;
import com.eminyidle.place.place.dto.node.Tour;
import com.eminyidle.place.place.exception.KafkaDataNotExistException;
import com.eminyidle.place.place.service.PlaceService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
@RequiredArgsConstructor
public class KafkaPlace {

    private final PlaceService placeService;

    @Value("${KAFKA_TOUR_ALERT_TOPIC")
    String tourAlertTopic;
    @Value("${KAFKA_PLACE_ALERT_TOPIC}")
    String KAFKA_PLACE_ALERT_TOPIC;

    @KafkaListener(topics = "${KAFKA_TOUR_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    public void receiveTourAlert(ConsumerRecord<String, String> consumerRecord) {

//        if(consumerRecord.key().isBlank() || consumerRecord.value().isBlank()) {
//            throw new KafkaDataNotExistException("데이터가 없습니다.");
//        }
        log.info("tour kafka 시작");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            KafkaMessage message = objectMapper.readValue(consumerRecord.value(), KafkaMessage.class);
            Map<String, Object> tourMap = (Map<String, Object>) message.getBody();
            log.info(message.getType());
            switch (message.getType()) {
                case "CREATE":
                    log.info("create start");
                    log.info(tourMap.toString());
                    List<Integer> dateStart = (List<Integer>) tourMap.get("startDate");
                    log.info(dateStart.toString());
                    List<Integer> dateEnd = (List<Integer>) tourMap.get("endDate");
                    log.info(String.format("%02d", dateStart.get(1)));
                    String start = dateStart.get(0) + "-" + String.format("%02d", dateStart.get(1)) + "-" + String.format("%02d", dateStart.get(2)) + "T00:00:00";
                    String end = dateEnd.get(0) + "-" + String.format("%02d", dateEnd.get(1)) + "-" + String.format("%02d", dateEnd.get(2)) + "T00:00:00";
                    log.info(LocalDateTime.parse(end).toString());
                    log.info((String) tourMap.get("tourTitle"));
                    Tour tour = Tour.builder()
                            .tourId((String) tourMap.get("tourId"))
                            .tourTitle((String) tourMap.get("tourTitle"))
                            .startDate(LocalDateTime.parse(start))
                            .endDate(LocalDateTime.parse(end))
                            .build();

                    placeService.createTour(tour);
                    break;
                case "UPDATE":
                    break;
                default:
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
    }

    @KafkaListener(topics = "${KAFKA_PLACE_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    public void receivePlaceAlert(ConsumerRecord<String, String> consumerRecord) {
        log.info("place kafka 시작");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            KafkaMessage message = objectMapper.readValue(consumerRecord.value(), KafkaMessage.class);
            Map<String, Object> placeMap = (Map<String, Object>) message.getBody();
            log.info(message.getType());
            log.info(placeMap.toString());
        } catch (Exception e) {
            log.error("{}", e);
        }
    }
}
