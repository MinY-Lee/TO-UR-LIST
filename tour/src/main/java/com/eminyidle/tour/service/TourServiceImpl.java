package com.eminyidle.tour.service;

import com.eminyidle.tour.dto.Attend;
import com.eminyidle.tour.dto.Tour;
import com.eminyidle.tour.dto.TourDetail;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateTourReq;
import com.eminyidle.tour.dto.req.UpdateTourCityReq;
import com.eminyidle.tour.dto.req.UpdateTourPeriodReq;
import com.eminyidle.tour.dto.req.UpdateTourTitleReq;
import com.eminyidle.tour.repository.CityRepository;
import com.eminyidle.tour.repository.TourRepository;
import com.eminyidle.tour.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TourServiceImpl implements TourService, UserService {

    private final TourRepository tourRepository;
    private final UserRepository userRepository;
    private final CityRepository cityRepository;

    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(String userId) {

    }

    //TODO - User로 받으면 좋겠다! 애초에 닉네임, 네임 넣어두도록!!
    @Override
    public Tour createTour(User user, CreateTourReq createTourReq) {
        //만약 내 DB에 user있는지 확인
        //있다면 그거 챙겨온다
        //없다면 유저주라! 한 뒤 유저노드 만들기
        log.debug(createTourReq.toString());
        Tour tour = Tour.builder()
                .tourId(UUID.randomUUID().toString())
                .tourTitle(createTourReq.getTourTitle())
                .startDate(createTourReq.getStartDate())
                .endDate(createTourReq.getEndDate())
                .cityList(createTourReq.getCityList().stream().map(
                        (city)->
                            cityRepository.findCity(city.getCityName(),city.getCountryCode()).orElse(city)
                ).toList())
                .build();
        tourRepository.save(tour);

        userRepository.findById(user.getUserId()).ifPresent((dbUser)->{
            user.setTourList(dbUser.getTourList());
        });

        user.getTourList().add(Attend.builder()
                .tourTitle(createTourReq.getTourTitle())
                .tour(tour)
                .build());
        log.debug(user.toString());
        userRepository.save(user);


        return tour;
    }

    @Override
    public void deleteTour(String tourId) {
        // host여야 삭제 가능. 삭제 시, 연결된 모든 tourActivity도 지워져야 한다
    }

    @Override
    public void updateTourTitle(String userId, UpdateTourTitleReq updateTourTitleReq) {
        //ATTEND관계에서 제목 업데이트
    }

    @Override
    public void updateTourPeriod(String userId, UpdateTourPeriodReq updateTourPeriodReq) {

    }

    @Override
    public void updateTourCity(String userId, UpdateTourCityReq updateTourCityReq) {
        //DB에 도시 있으면 그 도시와 연결하고.. 없으면 새로운 도시 노드 만들어서 맞는 국가랑 연결..
    }
    @Override
    public Tour searchTour(String userId, String tourId) {
        return tourRepository.findById(tourId).orElse(new Tour());
    }
    @Override
    public TourDetail searchTourDetail(String userId, String tourId) {
        System.out.println(searchTour(userId,tourId));
        return null;
    }

    @Override
    public List<Tour> searchTourList(String userId) {
        //userId가 attend 중인 모든 관계
        return null;
    }

    @Override
    public void quitTour(String userId, String tourId) {
        // user-Tour 관계(attend) 삭제!
        // host 아닌 것 확인 필수
        // tour에 연결된 모든 attend관계가 사라졌을 때 delete처리
        // 여행 완료된 상태가 아니라면, member관계도 삭제
    }
}
