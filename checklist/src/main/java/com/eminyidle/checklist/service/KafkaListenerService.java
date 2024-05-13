package com.eminyidle.checklist.service;

import com.eminyidle.checklist.dto.KafkaMessage;
import com.eminyidle.checklist.exception.KafkaDataNotExistException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaListenerService {

    @Value("${KAFKA_TOUR_ALERT_TOPIC}")
    private String KAFKA_TOUR_TOPIC;

    @Value("${KAFKA_PLACE_ALERT_TOPIC}")
    private String KAFKA_PLACE_TOPIC;

    @Value("${KAFKA_ACTIVITY_ALERT_TOPIC}")
    private String KAFKA_ACTIVITY_TOPIC;

    @KafkaListener(topics = "${KAFKA_TOUR_ALERT_TOPIC}", containerFactory = "kafkaListenerContainerFactory")
    private void consumeTourMessage(@Payload String kafkaMessage) {
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
                case "UPDATE":
                    break;
                case "DELETE":
                    break;
                case "QUIT":
                    break;
                default:

            }

        } catch (Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }
}
