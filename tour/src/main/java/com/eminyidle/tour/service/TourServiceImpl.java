package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.*;
import com.eminyidle.tour.dto.req.CreateTourReq;
import com.eminyidle.tour.dto.req.UpdateTourCityReq;
import com.eminyidle.tour.dto.req.UpdateTourPeriodReq;
import com.eminyidle.tour.dto.req.UpdateTourTitleReq;
import com.eminyidle.tour.exception.AbnormalTourDateException;
import com.eminyidle.tour.exception.NoHostPrivilegesException;
import com.eminyidle.tour.exception.NoSuchCityException;
import com.eminyidle.tour.exception.NoSuchTourException;
import com.eminyidle.tour.repository.CityRepository;
import com.eminyidle.tour.repository.TourRepository;
import com.eminyidle.tour.repository.UserRepository;
import com.eminyidle.tour.repository.maria.CountryCityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
                                    countryCityRepository.findCityEntityByCountryCodeAndCityNameKor(city.getCountryCode(),city.getCityName()).orElseThrow(NoSuchCityException::new);
                                    return cityRepository.save(City.builder()
                                            .countryCode(city.getCountryCode())
                                            .cityName(city.getCityName())
                                            .build());
                                })
                ).toList())
                .build();
        tourRepository.save(tour);

        User user = userRepository.findById(userId).orElseGet(() ->
                requestService.getUser(userId)
        );

        user.getTourList().add(Attend.builder()
                .tourTitle(createTourReq.getTourTitle())
                .tour(tour)
                .build());
//        log.debug(user.toString());
        userRepository.save(user);
        userRepository.createMemberRelationship(user.getUserId(), tour.getTourId(), "host");

        // TODO - 나라와 연계된 체크리스트 생성(Kafka)
        return tour;
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
                            countryCityRepository.findCityEntityByCountryCodeAndCityNameKor(city.getCountryCode(),city.getCityName()).orElseThrow(NoSuchCityException::new);
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
        if (tour.getEndDate().isBefore(LocalDateTime.now())) { //여행 후인 경우 - 다른 멤버들에게는 연결 정보 유지: 유저이지만, 타입을 고스트로 변경
            userRepository.createMemberRelationship(userId, tourId, "ghost");
        }
    }
}
