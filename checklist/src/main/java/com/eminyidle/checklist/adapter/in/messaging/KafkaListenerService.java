package com.eminyidle.checklist.adapter.in.messaging;

import com.eminyidle.checklist.adapter.in.messaging.dto.City;
import com.eminyidle.checklist.adapter.in.messaging.dto.TourMember;
import com.eminyidle.checklist.adapter.in.messaging.dto.TourPlaceNode;
import com.eminyidle.checklist.adapter.in.messaging.dto.req.*;
import com.eminyidle.checklist.adapter.in.messaging.dto.TourNode;
import com.eminyidle.checklist.application.service.ChecklistServiceImpl;
import com.eminyidle.checklist.exception.CreateTourException;
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
                    if(message.getDesc()==null){
                        throw new CreateTourException();
                    }
                    log.debug("hostUserId: "+message.getDesc());
                    checklistService.createTour(tour.getTourId(), Duration.between(tour.getStartDate(), tour.getEndDate()).toDays() + 1, tour.getCityList().stream().map(City::getCountryCode).collect(Collectors.toSet()),message.getDesc());
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
            TourMemberKafkaMessage message = objectMapper.readValue(kafkaMessage, TourMemberKafkaMessage.class);
            log.debug("-->" + message.toString());
            TourMember member=message.getBody();
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour member");
                    checklistService.createMember(member.getTourId(),member.getUserId());
                    break;
                case "DELETE":
                    checklistService.deleteMember(member.getTourId(),member.getUserId());
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
            TourPlaceKafkaMessage message = objectMapper.readValue(kafkaMessage, TourPlaceKafkaMessage.class);
            log.debug("-->" + message.toString());

            TourPlaceNode tourPlace = message.getBody();
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created tour");
                    log.debug(tourPlace.toString());
                    checklistService.createPlace(tourPlace.getTourId(),tourPlace.getTourPlaceId(),tourPlace.getPlaceId(), tourPlace.getTourDay());
                    break;
                case "DELETE":
                    checklistService.deletePlace(tourPlace.getTourId(),tourPlace.getPlaceId(), tourPlace.getTourDay());
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
            TourActivityKafkaMessage message = objectMapper.readValue(kafkaMessage, TourActivityKafkaMessage.class);
            log.debug("-->" + message.toString());

            TourActivityNode activityNode = message.getBody();
            switch (message.getType()) {
                case "CREATE":
                    log.debug("created activity");
                    if(activityNode.getTourPlaceId()==null){
                        checklistService.createActivity(activityNode.getTourId(), activityNode.getPlaceId(), activityNode.getTourDay(), activityNode.getActivity());
                    } else{
                        checklistService.createActivity(activityNode.getTourPlaceId(),activityNode.getActivity());
                    }
                    break;
                case "DELETE":
                    log.debug("DELETE activity");
                    checklistService.deleteActivity(activityNode.getTourPlaceId(),activityNode.getActivity());
                    break;
                default:

            }

        } catch (JsonProcessingException e) {
            throw new KafkaDataNotExistException("consumeActivityMessage-Message 오류가 발생했습니다.");
        }
    }
}
