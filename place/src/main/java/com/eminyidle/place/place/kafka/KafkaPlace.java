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

import java.time.Duration;
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

            LocalDateTime start;
            LocalDateTime end;

            log.info(message.getType());
            switch (message.getType()) {
                case "CREATE":
                    log.info("create start");
                    log.info(tourMap.toString());
//                    List<Integer> dateStart = (List<Integer>) tourMap.get("startDate");
//                    log.info(dateStart.toString());
//                    List<Integer> dateEnd = (List<Integer>) tourMap.get("endDate");
                    start = LocalDateTime.parse((String) tourMap.get("startDate"));
                    log.info(start.toString());
                    end = LocalDateTime.parse((String) tourMap.get("endDate"));

                    Duration duration = Duration.between(start,end);
                    Integer tourPeriod = (int) duration.getSeconds()/(60*60*24) + 1;
                    log.info(tourPeriod.toString());
                    Tour tour = Tour.builder()
                            .tourId((String) tourMap.get("tourId"))
                            .tourTitle((String) tourMap.get("tourTitle"))
                            .startDate(start)
                            .endDate(end)
                            .tourPeriod(tourPeriod)
                            .build();
                    placeService.createTour(tour);
                    break;
                case "UPDATE_DATE":
                    log.info("update start");
                    log.info(tourMap.toString());

                    start = LocalDateTime.parse((String) tourMap.get("startDate"));
                    log.info(start.toString());
                    end = LocalDateTime.parse((String) tourMap.get("endDate"));

                    Duration durationDate = Duration.between(start,end);
                    Integer newPeriod = (int) durationDate.getSeconds()/(60*60*24) + 1;
                    Tour newTour = Tour.builder()
                            .tourId((String) tourMap.get("tourId"))
                            .tourTitle((String) tourMap.get("tourTitle"))
                            .startDate(start)
                            .endDate(end)
                            .tourPeriod(newPeriod)
                            .build();
                    placeService.updateTour((String) tourMap.get("tourId"), (String) tourMap.get("tourTitle"), start, end, newPeriod);
                    break;
                case "DELETE":
                    log.info("delete start");
                    log.info(tourMap.toString());
                    // TourId 를 받아와서 해당하는 Tour 지워주기
                    String tourId = (String) tourMap.get("tourId");
                    placeService.deleteTour(tourId);
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
