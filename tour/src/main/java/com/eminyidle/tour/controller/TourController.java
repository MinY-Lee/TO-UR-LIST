package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.Attend;
import com.eminyidle.tour.dto.Tour;
import com.eminyidle.tour.dto.TourDetail;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateTourReq;
import com.eminyidle.tour.dto.req.UpdateTourCityReq;
import com.eminyidle.tour.dto.req.UpdateTourPeriodReq;
import com.eminyidle.tour.dto.req.UpdateTourTitleReq;
import com.eminyidle.tour.dto.res.SearchTourDetailRes;
import com.eminyidle.tour.exception.UserInfoInRequestNotFoundException;
import com.eminyidle.tour.service.TourService;
import com.eminyidle.tour.service.TourServiceImpl;
import com.eminyidle.tour.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;
    private final String HEADER_USER_ID="userId";

    @PostMapping()
    public ResponseEntity<String> createTour(@RequestBody CreateTourReq createTourReq, @RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("createTour");
        User user= null;//getUserFromHeader(header);
        Tour tour=tourService.createTour(user,createTourReq);
        return ResponseEntity.ok(tour.getTourId());
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<SearchTourDetailRes> searchTourDetail(@PathVariable String tourId, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("searchTour");
        TourDetail tourDetail=tourService.searchTourDetail(userId, tourId);
        return ResponseEntity.ok(
                SearchTourDetailRes.builder()
                        .tourTitle(tourDetail.getTourTitle())
                        .startDate(tourDetail.getStartDate())
                        .endDate(tourDetail.getEndDate())
                        .cityList(tourDetail.getCityList())
                        .memberList(tourDetail.getMemberList())
                        .build()
        );
    }

    @DeleteMapping("/{tourId}")
    public ResponseEntity<Void> deleteTour(@PathVariable String tourId, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("deleteTour "+tourId);
        tourService.deleteTour(userId, tourId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/title")
    public ResponseEntity<Void> updateTourTitle(@RequestBody UpdateTourTitleReq updateTourTitleReq, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("updateTourTitle ");
        tourService.updateTourTitle(userId, updateTourTitleReq);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/period")
    public ResponseEntity<Void> updateTourPeriod(@RequestBody UpdateTourPeriodReq updateTourPeriodReq, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("updateTourPeriod ");
        tourService.updateTourPeriod(userId, updateTourPeriodReq);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/city")
    public ResponseEntity<Void> updateTourCity(@RequestBody UpdateTourCityReq updateTourCityReq, @RequestHeader(HEADER_USER_ID) String userId){
        log.debug("updateTourCity ");
        tourService.updateTourCity(userId, updateTourCityReq);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    public ResponseEntity<List<Tour>> searchTourList(@RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("searchTourList: "+userId);
        List<Tour> tourList=tourService.searchTourList(userId);
        return ResponseEntity.ok(tourList);
    }
    @DeleteMapping() //TODO - 이렇게 정의하는게 맞을지 체크
    public ResponseEntity<Void> quitTour(@RequestBody String tourId, @RequestHeader(HEADER_USER_ID) String userId) {
        log.debug("quitTour");
        return ResponseEntity.ok().build();
    }




    //FEED
    //새 여행으로 가져오기 -> 피드번호에 맞는 tourId와 제목, 시작하는 날, 끝나는 날 알려주면...
        //그 도시 바탕으로 현재 유저에게 새로운 여행 만들어준다
        //도시는 알아서 그 도시로,
    //기존 여행으로 가져오기 -> 피드에 맞게 도시 "추가"
}
