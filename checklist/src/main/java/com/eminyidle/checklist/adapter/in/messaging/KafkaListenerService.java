package com.eminyidle.checklist.adapter.in.messaging;

import com.eminyidle.checklist.adapter.in.messaging.dto.req.KafkaMessage;
import com.eminyidle.checklist.adapter.in.messaging.dto.TourNode;
import com.eminyidle.checklist.adapter.in.messaging.dto.req.TourKafkaMessage;
import com.eminyidle.checklist.exception.KafkaDataNotExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaListenerService {

    @KafkaListener(topics = "${KAFKA_TOUR_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeTourMessage(@Payload String kafkaMessage) {
        log.debug("consume: createTour");
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            TourKafkaMessage message = objectMapper.readValue(kafkaMessage, TourKafkaMessage.class);
            log.debug("-->" + message);
            TourNode tour = message.getBody();
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour");
                    log.debug(tour.toString());
                    //ㄴ이렇게 도달: {tourId=a646cbf3-c6f9-4613-9e9f-ae4a4a533a8b, tourTitle=pppp, startDate=[2023, 10, 1, 0, 0], endDate=[2023, 10, 1, 0, 0], cityList=[{id=4:58ac303d-e4b5-4f2b-9bfa-7a55d99245cc:0, countryCode=JPN, cityName=교토시}]}
                    log.debug(tour.getCityList().toString());
                    log.debug(String.valueOf(tour.getStartDate()));
                    //tourMap.get("startDate")하면 문제 생김
                    break;
                case "UPDATE_CITY":
                    break;
                case "DELETE":
                    break;
                default:

            }

        } catch (Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }

    @KafkaListener(topics = "${KAFKA_MEMBER_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeMemberMessage(@Payload String kafkaMessage) {
        log.debug("consume: createTour");
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

        } catch (Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }

    @KafkaListener(topics = "${KAFKA_PLACE_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumePlaceMessage(@Payload String kafkaMessage) {
        log.debug("consume: createTour");
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

        } catch (Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }

    @KafkaListener(topics = "${KAFKA_ACTIVITY_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeActivityMessage(@Payload String kafkaMessage) {
        log.debug("consume: createTour");
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

        } catch (Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }
}
