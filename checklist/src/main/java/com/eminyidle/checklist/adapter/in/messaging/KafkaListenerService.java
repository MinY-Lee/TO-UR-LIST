package com.eminyidle.checklist.adapter.in.messaging;

import com.eminyidle.checklist.adapter.in.messaging.dto.City;
import com.eminyidle.checklist.adapter.in.messaging.dto.req.KafkaMessage;
import com.eminyidle.checklist.adapter.in.messaging.dto.TourNode;
import com.eminyidle.checklist.adapter.in.messaging.dto.req.TourKafkaMessage;
import com.eminyidle.checklist.application.service.ChecklistServiceImpl;
import com.eminyidle.checklist.exception.KafkaDataNotExistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaListenerService {

    private final ChecklistServiceImpl checklistService;

    @KafkaListener(topics = "${KAFKA_TOUR_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeTourMessage(@Payload String kafkaMessage) {
        log.debug("consumeTourMessage");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TourKafkaMessage message = objectMapper.readValue(kafkaMessage, TourKafkaMessage.class);
            log.debug("-->" + message);
            TourNode tour = message.getBody();
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour");
                    log.debug(tour.toString());
                    log.debug(tour.getCityList().toString());
                    log.debug(String.valueOf(tour.getStartDate()));
                    log.debug(String.valueOf(Duration.between(tour.getStartDate(), tour.getEndDate()).toDays()));
                    checklistService.createTour(tour.getTourId(), Duration.between(tour.getStartDate(), tour.getEndDate()).toDays() + 1, tour.getCityList().stream().map(City::getCountryCode).collect(Collectors.toSet()));
                    break;
                case "UPDATE_CITY":
                    log.debug("update country");
                    checklistService.updateCountry(tour.getTourId(), tour.getCityList().stream().map(City::getCountryCode).collect(Collectors.toSet()));
                    break;
                case "DELETE":
                    log.debug("delete tour");
                    checklistService.deleteTour(tour.getTourId());
                    break;
                default:

            }

        } catch (JsonProcessingException e) {
            throw new KafkaDataNotExistException("consumeTourMessage-Message 오류가 발생했습니다.");
        }
    }

    @KafkaListener(topics = "${KAFKA_MEMBER_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeMemberMessage(@Payload String kafkaMessage) {
        log.debug("consumeMemberMessage");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.debug("try to deserialize..." + kafkaMessage);
            KafkaMessage message = objectMapper.readValue(kafkaMessage, KafkaMessage.class);
            log.debug("readValue success");
            Map<String, Object> tourMap = (Map<String, Object>) message.getBody();
            log.debug("-->" + message.toString());
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour");
                    log.debug(tourMap.toString());
                    //ㄴ이렇게 도달: {tourId=a646cbf3-c6f9-4613-9e9f-ae4a4a533a8b, tourTitle=pppp, startDate=[2023, 10, 1, 0, 0], endDate=[2023, 10, 1, 0, 0], cityList=[{id=4:58ac303d-e4b5-4f2b-9bfa-7a55d99245cc:0, countryCode=JPN, cityName=교토시}]}
                    log.debug((String) tourMap.get("tourTitle"));
                    //tourMap.get("startDate")하면 문제 생김
                    break;
                case "DELETE":
                    break;
                default:

            }

        } catch (JsonProcessingException e) {
            throw new KafkaDataNotExistException("consumeMemberMessage-Message 오류가 발생했습니다.");
        }
    }

    @KafkaListener(topics = "${KAFKA_PLACE_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumePlaceMessage(@Payload String kafkaMessage) {
        log.debug("consumePlaceMessage");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.debug("try to deserialize..." + kafkaMessage);
            KafkaMessage message = objectMapper.readValue(kafkaMessage, KafkaMessage.class);
            log.debug("readValue success");
            Map<String, Object> tourMap = (Map<String, Object>) message.getBody();
            log.debug("-->" + message.toString());
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour");
                    log.debug(tourMap.toString());
                    //ㄴ이렇게 도달: {tourId=a646cbf3-c6f9-4613-9e9f-ae4a4a533a8b, tourTitle=pppp, startDate=[2023, 10, 1, 0, 0], endDate=[2023, 10, 1, 0, 0], cityList=[{id=4:58ac303d-e4b5-4f2b-9bfa-7a55d99245cc:0, countryCode=JPN, cityName=교토시}]}
                    log.debug((String) tourMap.get("tourTitle"));
                    //tourMap.get("startDate")하면 문제 생김
                    break;
                case "DELETE":
                    break;
                default:

            }

        } catch (JsonProcessingException e) {
            throw new KafkaDataNotExistException("consumePlaceMessage-Message 오류가 발생했습니다.");
        }
    }

    @KafkaListener(topics = "${KAFKA_ACTIVITY_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeActivityMessage(@Payload String kafkaMessage) {
        log.debug("consumeActivityMessage");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            log.debug("try to deserialize..." + kafkaMessage);
            KafkaMessage message = objectMapper.readValue(kafkaMessage, KafkaMessage.class);
            log.debug("readValue success");
            Map<String, Object> tourMap = (Map<String, Object>) message.getBody();
            log.debug("-->" + message.toString());
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour");
                    log.debug(tourMap.toString());
                    //ㄴ이렇게 도달: {tourId=a646cbf3-c6f9-4613-9e9f-ae4a4a533a8b, tourTitle=pppp, startDate=[2023, 10, 1, 0, 0], endDate=[2023, 10, 1, 0, 0], cityList=[{id=4:58ac303d-e4b5-4f2b-9bfa-7a55d99245cc:0, countryCode=JPN, cityName=교토시}]}
                    log.debug((String) tourMap.get("tourTitle"));
                    //tourMap.get("startDate")하면 문제 생김
                    break;
                case "DELETE":
                    break;
                default:

            }

        } catch (JsonProcessingException e) {
            throw new KafkaDataNotExistException("consumeActivityMessage-Message 오류가 발생했습니다.");
        }
    }
}
