package com.eminyidle.tour.application.service;

import com.eminyidle.tour.adapter.out.messaging.TourKafkaProducer;
import com.eminyidle.tour.adapter.out.persistence.neo4j.GhostRepository;
import com.eminyidle.tour.adapter.out.web.RequestService;
import com.eminyidle.tour.application.dto.*;
import com.eminyidle.tour.application.dto.req.*;
import com.eminyidle.tour.application.port.MemberService;
import com.eminyidle.tour.application.port.TourService;
import com.eminyidle.tour.application.dto.TourMember;
import com.eminyidle.tour.application.port.UserUpdateUsecase;
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
public class TourServiceImpl implements TourService, MemberService, UserUpdateUsecase {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;
    private final CountryCityRepository countryCityRepository;
    private final GhostRepository ghostRepository;

    private final RequestService requestService;
    private final TourKafkaProducer kafkaProducer;

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

        // 나라와 연계된 체크리스트 생성(Kafka)
        kafkaProducer.produceCreateTour(tour, userId);
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
        kafkaProducer.produceDeleteTour(new Tour(tourId));
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
        tour.setTourDate(updateTourPeriodReq.getStartDate(),updateTourPeriodReq.getEndDate());
        tourRepository.save(tour);
        //Kafka
        kafkaProducer.produceUpdateTourDate(tour);
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
            updateHost(userId, TourMember.builder()
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

    // =============== MEMBER SERVICE ===============================
    private void assertHost(String userId, String tourId) {
        String memberType = userRepository.findMemberTypeByUserIdAndTourId(userId, tourId);
        if (!"host".equals(memberType)) {
            throw new NoHostPrivilegesException();
        }
    }

    private void assertAttend(String userId, String tourId) {
        userRepository.findUserByAttendRelationship(userId, tourId).orElseThrow(UserNotAttendSuchTourException::new);
    }

    private boolean isAttend(String userId, String tourId) {
        return userRepository.existsAttendRelationshipByUserIdAndTourId(userId, tourId);
    }

    private boolean isExistUser(String userId) {
        return userRepository.existsById(userId);
    }

    //Refactor하면 좋을듯..
    @Override
    public void createMember(String hostId, TourMember tourMember) {
        assertHost(hostId, tourMember.getTourId());
        if (isExistUser(tourMember.getUserId()) && isAttend(tourMember.getUserId(), tourMember.getTourId())) {
            throw new AlreadyUserAttendTourException();
        }
        Tour tour = tourRepository.findById(tourMember.getTourId()).orElseThrow(NoSuchTourException::new);
        User user = userRepository.findById(tourMember.getUserId())
                .orElseGet(() ->
                        userRepository.save(
                                requestService.getUser(tourMember.getUserId())
                        )
                );
        userRepository.createGuestRelationship(hostId, tour.getTourId(), user.getUserId());
        // 연결된 모든 체크리스트 생성 (Kafka)
        kafkaProducer.produceCreateMember(user.getUserId(), tour.getTourId());
    }

    @Override
    public Ghost createGhostMember(String hostId, CreateGhostMemberReq createGhostMemberReq) {
        assertHost(hostId, createGhostMemberReq.getTourId());

        if (ghostRepository.existsGhostByGhostNicknameAndTourId(createGhostMemberReq.getGhostNickname(), createGhostMemberReq.getTourId())) {
            throw new DuplicatedGhostNicknameException();
        }

        Ghost ghost = ghostRepository.save(Ghost.builder()
                .ghostId(UUID.randomUUID().toString())
                .ghostNickname(createGhostMemberReq.getGhostNickname())
                .build());
        ghostRepository.createGhostRelationship(ghost.getGhostId(), createGhostMemberReq.getTourId());
        return ghost;
    }

    @Override
    public void updateGhostMemberNickname(String hostId, UpdateGhostMemberReq updateGhostMemberReq) {
        assertHost(hostId, updateGhostMemberReq.getTourId());

        Ghost ghost = ghostRepository.findById(updateGhostMemberReq.getGhostId()).orElseThrow(NoSuchGhostException::new);
        ghost.setGhostNickname(updateGhostMemberReq.getGhostNickname());
        ghostRepository.save(ghost);
    }

    @Override
    public List<Member> searchMemberList(String userId, String tourId) {
        assertAttend(userId, tourId);

        List<Member> memberList = userRepository.findMembersByTourId(tourId);
        if (memberList.isEmpty()) throw new NoSuchTourException();
        return memberList;
    }

    @Override
    public void updateGhostToGuest(String hostId, UpdateGhostToGuestReq updateGhostToGuestReq) {
        Ghost ghost = ghostRepository.findById(updateGhostToGuestReq.getGhostId()).orElseThrow(NoSuchGhostException::new);

        //여기에서 host임이 보장된다
        createMember(hostId, TourMember.builder()
                .userId(updateGhostToGuestReq.getUserId())
                .tourId(updateGhostToGuestReq.getTourId())
                .userNickname(updateGhostToGuestReq.getUserNickname())
                .build()
        );
        // ghost에 있던 연결(가계부) guest에 잇기 : Kafka
        kafkaProducer.produceGhostToGuest(updateGhostToGuestReq.getTourId(), updateGhostToGuestReq.getGhostId(), updateGhostToGuestReq.getUserId());

        ghostRepository.delete(ghost);
    }

    @Override
    public void updateHost(String hostId, TourMember tourMember) {
        assertHost(hostId, tourMember.getTourId());

        //참여중인 유저가 아니라면, 권한을 넘겨줄 수 없다.
        User user = userRepository.findUserByAttendRelationship(tourMember.getUserId(), tourMember.getTourId()).orElseThrow(NoSuchMemberException::new);

        userRepository.updateMemberRelationshipExceptGhost(hostId, tourMember.getTourId(), "guest");
        userRepository.updateMemberRelationshipExceptGhost(user.getUserId(), tourMember.getTourId(), "host");

    }

    @Override
    public void deleteMember(String hostId, DeleteMemberReq deleteMemberReq) {
        assertHost(hostId, deleteMemberReq.getTourId());

        switch (deleteMemberReq.getMemberType()) {
            case "host":
                throw new HostCanNotBeDeletedException();
            case "guest":
                userRepository.deleteGuestRelationship(deleteMemberReq.getUserId(), deleteMemberReq.getTourId());
                // 모든 아이템 삭제 요청(KAFKA)
                kafkaProducer.produceDeleteMember(deleteMemberReq.getUserId(), deleteMemberReq.getTourId());
                break;
            case "ghost":
                ghostRepository.deleteById(deleteMemberReq.getUserId());
                break;
            default:
                throw new UndefinedMemberTypeException();
        }
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);
    }
}
