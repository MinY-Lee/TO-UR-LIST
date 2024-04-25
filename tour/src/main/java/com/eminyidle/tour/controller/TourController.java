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

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/tour")
public class TourController {

    private final TourService tourService;
    private final UserService userService;
    @GetMapping("/{userId}")
    public void createTour(@PathVariable String userId, @RequestBody CreateTourReq createTourReq){
        log.debug("createTour");
        tourService.createTour(userId,createTourReq);
        return;
    }

    @GetMapping("user/{userId}")
    public void createUser(@PathVariable String userId){
        log.debug("createUser");


        userService.createUser(User.builder()
                        .userId(userId)
                        .userName("TESTMAN")
                        .userNickname("haha")
                .build());
        return;
    }
}
