package com.eminyidle.tour.controller;

import com.eminyidle.tour.dto.Attend;
import com.eminyidle.tour.dto.User;
import com.eminyidle.tour.dto.req.CreateTourReq;
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
    public void createTour(@RequestBody CreateTourReq createTourReq, @RequestHeader Map<String,String> header) throws UnsupportedEncodingException {
        log.debug("createTour");
        log.debug(">>"+header);
        User user= User.builder()
                .userId(header.get("userid"))
                .userNickname(URLDecoder.decode(header.get("usernickname"),"UTF-8"))
                .userName(URLDecoder.decode(header.get("username"),"UTF-8"))
                .tourList(new ArrayList<>())
                .build();
        tourService.createTour(user,createTourReq);
        return;
    }

    @GetMapping("/{tourId}")
    public void searchTour(@PathVariable String tourId){
        log.debug("searchTour");
        log.debug(tourService.searchTour("", tourId).toString());
    }
}
