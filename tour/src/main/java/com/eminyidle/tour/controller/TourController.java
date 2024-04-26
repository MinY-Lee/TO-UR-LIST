package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.Attend;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateTourReq;
import com.eminyidle.tour.exception.UserInfoInRequestNotFoundException;
import com.eminyidle.tour.service.TourService;
import com.eminyidle.tour.service.TourServiceImpl;
import com.eminyidle.tour.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    @GetMapping()
    public void createTour(@RequestBody CreateTourReq createTourReq, @RequestHeader Map<String,String> header) {
        log.debug("createTour");
        log.debug(">>"+header);
        User user= getUserFromHeader(header);
        tourService.createTour(user,createTourReq);
    }

    @GetMapping("/{tourId}")
    public void searchTour(@PathVariable String tourId, @RequestHeader Map<String,String> header){
        log.debug("searchTour");
        log.debug(tourService.searchTour(header.get("userid"), tourId).toString());
    }

    @DeleteMapping("/{tourId}")
    public void deleteTour(@PathVariable String tourId, @RequestHeader Map<String,String> header){
        log.debug("deleteTour "+tourId);
        tourService.deleteTour(header.get("userid"), tourId);
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
