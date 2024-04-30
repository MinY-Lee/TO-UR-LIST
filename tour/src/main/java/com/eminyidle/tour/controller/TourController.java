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

    @PostMapping()
    public ResponseEntity<String> createTour(@RequestBody CreateTourReq createTourReq, @RequestHeader Map<String,String> header) {
        log.debug("createTour");
        log.debug(">>"+header);
        User user= getUserFromHeader(header);
        Tour tour=tourService.createTour(user,createTourReq);
        return ResponseEntity.ok(tour.getTourId());
    }

    @GetMapping("/{tourId}")
    public ResponseEntity<SearchTourDetailRes> searchTourDetail(@PathVariable String tourId, @RequestHeader Map<String,String> header){
        log.debug("searchTour");
        TourDetail tourDetail=tourService.searchTourDetail(header.get("userid"), tourId);
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
    public ResponseEntity<Void> deleteTour(@PathVariable String tourId, @RequestHeader Map<String,String> header){
        log.debug("deleteTour "+tourId);
        tourService.deleteTour(header.get("userid"), tourId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/title")
    public ResponseEntity<Void> updateTourTitle(@RequestBody UpdateTourTitleReq updateTourTitleReq, @RequestHeader Map<String,String> header){
        log.debug("updateTourTitle ");
        User user=getUserFromHeader(header);
        tourService.updateTourTitle(user.getUserId(), updateTourTitleReq);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/period")
    public ResponseEntity<Void> updateTourPeriod(@RequestBody UpdateTourPeriodReq updateTourPeriodReq, @RequestHeader Map<String,String> header){
        log.debug("updateTourPeriod ");
        User user=getUserFromHeader(header);
        tourService.updateTourPeriod(user.getUserId(), updateTourPeriodReq);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/city")
    public ResponseEntity<Void> updateTourCity(@RequestBody UpdateTourCityReq updateTourCityReq, @RequestHeader Map<String,String> header){
        log.debug("updateTourCity ");
        User user=getUserFromHeader(header);
        tourService.updateTourCity(user.getUserId(), updateTourCityReq);
        return ResponseEntity.ok().build();
    }
    @GetMapping()
    public ResponseEntity<List<Tour>> searchTourList(@RequestHeader Map<String,String> header) {
        log.debug("searchTourList");
        log.debug(">>"+header);
        User user= getUserFromHeader(header);
        List<Tour> tourList=tourService.searchTourList(user.getUserId());
        return ResponseEntity.ok(tourList);
    }
    @DeleteMapping() //TODO - 이렇게 정의하는게 맞을지 체크
    public ResponseEntity<Void> quitTour(@RequestBody String tourId, @RequestHeader Map<String,String> header) {
        log.debug("quitTour");
        log.debug(">>"+header);
        User user= getUserFromHeader(header);
        return ResponseEntity.ok().build();
    }




    private User getUserFromHeader(Map<String,String> header){
        try {
            return User.builder()
                    .userId(header.get("userid"))
                    .userNickname(URLDecoder.decode(header.get("usernickname"),"UTF-8"))
                    .userName(URLDecoder.decode(header.get("username"),"UTF-8"))
                    .tourList(new ArrayList<>())
                    .build();
        } catch (UnsupportedEncodingException | NullPointerException e) {
            throw new UserInfoInRequestNotFoundException(e.getMessage());
        }
    }
}
