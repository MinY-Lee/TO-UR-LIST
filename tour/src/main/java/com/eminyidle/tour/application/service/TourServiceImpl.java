package com.eminyidle.tour.application.service;

import com.eminyidle.tour.adapter.out.messaging.TourKafkaProducer;
import com.eminyidle.tour.adapter.out.web.RequestService;
import com.eminyidle.tour.application.dto.*;
import com.eminyidle.tour.application.port.MemberService;
import com.eminyidle.tour.application.port.TourService;
import com.eminyidle.tour.application.dto.req.CreateTourReq;
import com.eminyidle.tour.application.dto.req.UpdateTourCityReq;
import com.eminyidle.tour.application.dto.req.UpdateTourPeriodReq;
import com.eminyidle.tour.application.dto.req.UpdateTourTitleReq;
import com.eminyidle.tour.application.dto.TourMember;
import com.eminyidle.tour.domain.Member;
import com.eminyidle.tour.exception.*;
import com.eminyidle.tour.adapter.out.persistence.neo4j.CityRepository;
import com.eminyidle.tour.adapter.out.persistence.neo4j.TourRepository;
import com.eminyidle.tour.adapter.out.persistence.neo4j.UserRepository;
import com.eminyidle.tour.country.repository.CountryCityRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional("neo4jTransactionManager")
public class TourServiceImpl implements TourService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryCityRepository countryCityRepository;

    private final MemberService memberService;
    private final RequestService requestService;
    private final TourKafkaProducer kafkaProducer;
    @Value("${KAFKA_PAYMENT_REQUEST_TOPIC}")
    private String paymentTopic;

    @Override
    public Tour createTour(String userId, CreateTourReq createTourReq) {
//        log.debug(createTourReq.toString());
        Tour tour = Tour.builder()
                .tourId(UUID.randomUUID().toString())
                .tourTitle(createTourReq.getTourTitle())
                .startDate(createTourReq.getStartDate())
                .endDate(createTourReq.getEndDate())
                .cityList(createTourReq.getCityList().stream().map(
                        (city) ->
                                cityRepository.findCity(city.getCityName(), city.getCountryCode()).orElseGet(() -> {
                                    countryCityRepository.findCityEntityByCountryCodeAndCityNameKor(city.getCountryCode(), city.getCityName()).orElseThrow(NoSuchCityException::new);
                                    return cityRepository.save(City.builder()
                                            .countryCode(city.getCountryCode())
                                            .cityName(city.getCityName())
                                            .build());
                                })
                ).toList())
                .build();
        tourRepository.save(tour);

        User user = userRepository.findById(userId).orElseGet(() -> {
                    User dbUser = requestService.getUser(userId);
                    dbUser.setTourList(new ArrayList<>());
                    return dbUser;
                }

        );

        user.getTourList().add(Attend.builder()
                .tourTitle(createTourReq.getTourTitle())
                .tour(tour)
                .build());
//        log.debug(user.toString());
        userRepository.save(user);
        userRepository.createMemberRelationship(user.getUserId(), tour.getTourId(), "host");

        // TODO - 나라와 연계된 체크리스트 생성(Kafka)
//        kafkaProducer.produceTourKafkaMessage("CREATE", tour);
        kafkaProducer.produceCreateTour(tour);
        return tour;
    }

    @KafkaListener(topics = "create-tour")
    private void consumerTest(@Payload String message) {
        log.debug("consumer get! " + message);
    }

    @KafkaListener(topics = "create-tour-ob")
    private void consumerTest2(ConsumerRecord<String, String> consumerRecord) {
        log.debug("get consume Object!");
        if (consumerRecord.value().isBlank()) {
            throw new KafkaDataNotExistException("ID 데이터가 없습니다.");
        }

        try {
            ObjectMapper mapper = new ObjectMapper();
            Tour message = mapper.readValue(consumerRecord.value(), Tour.class);

            log.debug("tourId: {}", consumerRecord.key());
            log.debug(message.toString());
            // 고스트 유저를 실제 유저로 변경
        } catch (Exception e) {
            throw new KafkaDataNotExistException("Message 오류가 발생했습니다.");
        }
    }

    private boolean isHost(String userId, String tourId) {
//        tourRepository.findHostedTourByUserIdAndTourId(userId,tourId).orElseThrow(NoSuchTourException::new);
//        return true;
        String memberType = userRepository.findMemberTypeByUserIdAndTourId(userId, tourId);
        if ("host".equals(memberType)) {
            return true;
        }
        return false;
    }

    @Override
    public void deleteTour(String userId, String tourId) {
        log.debug(userId + " " + tourId);
        //host가 아니면 삭제 불가
        if (!isHost(userId, tourId)) throw new NoHostPrivilegesException();

        tourRepository.deleteById(tourId);
        //TODO - 연결된 모든 tourActivity도 지워져야 한다 (KAFKA)
//        kafkaProducer.produceTourKafkaMessage("DELETE", Tour.builder()
//                .tourId(tourId)
//                .build());
        kafkaProducer.produceDeleteTour(Tour.builder().tourId(tourId).build());
    }

    @Override
    public void updateTourTitle(String userId, UpdateTourTitleReq updateTourTitleReq) {
        //ATTEND관계에서 제목 업데이트
        tourRepository.updateTourTitle(userId, updateTourTitleReq.getTourId(), updateTourTitleReq.getTourTitle());
    }

    @Override
    public void updateTourPeriod(String userId, UpdateTourPeriodReq updateTourPeriodReq) {
        Tour tour = tourRepository.findByUserIdAndTourId(userId, updateTourPeriodReq.getTourId()).orElseThrow(NoSuchTourException::new);
        if (updateTourPeriodReq.getStartDate().isAfter(updateTourPeriodReq.getEndDate())) {
            throw new AbnormalTourDateException();
        }
        tour.setStartDate(updateTourPeriodReq.getStartDate());
        tour.setEndDate(updateTourPeriodReq.getEndDate());
        tourRepository.save(tour);
    }

    @Override //FIXME - 로직 변경
    public void updateTourCity(String userId, UpdateTourCityReq updateTourCityReq) {
        //DB에 도시 있으면 그 도시와 연결하고.. 없으면 새로운 도시 노드 만들어서 맞는 국가랑 연결..
        Tour tour = tourRepository.findByUserIdAndTourId(userId, updateTourCityReq.getTourId()).orElseThrow(NoSuchTourException::new);
        log.debug(tour.toString());
        Set<City> citySet = updateTourCityReq.getCityList().stream().map(
                (city) ->
                        cityRepository.findCity(city.getCityName(), city.getCountryCode()).orElseGet(() -> {
                            countryCityRepository.findCityEntityByCountryCodeAndCityNameKor(city.getCountryCode(), city.getCityName()).orElseThrow(NoSuchCityException::new);
                            log.debug(city.toString());
                            return cityRepository.save(City.builder()
                                    .countryCode(city.getCountryCode())
                                    .cityName(city.getCityName())
                                    .build());
                        })
        ).collect(Collectors.toSet());
        cityRepository.findCitiesByTourId(updateTourCityReq.getTourId()).stream().forEach(
                (city) -> {
//                    log.debug("chceking..." + city);
                    if (!citySet.contains(city)) {
                        tourRepository.deleteTourCityRelationshipByTourIdAndCityName(updateTourCityReq.getTourId(), city.getCityName());
                        log.debug("deleted " + city.getCityName());
                    }
                }
        );
        tour.setCityList(citySet.stream().toList());

        tourRepository.save(tour);

        // TODO - 나라와 연계된 체크리스트 업데이트(Kafka)
//        kafkaProducer.produceTourKafkaMessage("UPDATE", tour);
        kafkaProducer.produceUpdateTourCity(tour);
    }

    @Override
    public Tour searchTour(String userId, String tourId) {
        //만약 userId -ATTEND-> tourId 가 아니면 에러!
        return tourRepository.findById(tourId).orElse(new Tour());
    }

    @Override

    public TourDetail searchTourDetail(String userId, String tourId) {
        TourDetail tourDetail = tourRepository.findTourDetailByUserIdAndTourId(userId, tourId).orElseThrow(NoSuchTourException::new);
        tourDetail.setMemberList(userRepository.findMembersByTourId(tourId));
        tourDetail.setCityList(cityRepository.findCitiesByTourId(tourId));
        System.out.println(tourDetail);
        return tourDetail;
    }

    @Override
    public List<Tour> searchTourList(String userId) {
        return tourRepository.findAllToursByUserId(userId);
        //userId가 attend 중인 모든 관계
    }

    @Override
    public void quitTour(String userId, String tourId) {
        if (isHost(userId, tourId)) {
            List<Member> memberList = userRepository.findMembersByTourId(tourId).stream()
                    .filter(member -> "guest".equals(member.getMemberType())).toList();
            if (memberList.isEmpty()) { //guest 아무도 없음 -> 삭제 처리
                deleteTour(userId, tourId);
                return;
            }
            //게스트가 있다면 랜덤하게 다음 게스트에게 호스트 권한 위임
            memberService.updateHost(userId, TourMember.builder()
                    .tourId(tourId)
                    .userId(memberList.get(0).getUserId())
                    .userNickname(memberList.get(0).getUserNickname())
                    .build());
        }
        Tour tour = tourRepository.findByUserIdAndTourId(userId, tourId).orElseThrow(NoSuchTourException::new);
        userRepository.deleteGuestRelationship(userId, tourId);
        //TODO- 관련 모든 아이템 삭제(KAFKA)
        kafkaProducer.produceDeleteMember(userId,tourId);
        if (tour.getEndDate().isBefore(LocalDateTime.now())) { //여행 후인 경우 - 다른 멤버들에게는 연결 정보 유지: 유저이지만, 타입을 고스트로 변경
            userRepository.createMemberRelationship(userId, tourId, "ghost");
        }
    }
}
