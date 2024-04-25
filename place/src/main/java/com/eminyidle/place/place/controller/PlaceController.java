package com.eminyidle.place.place.controller;

import com.eminyidle.place.place.dto.Activity;
import com.eminyidle.place.place.service.ActivityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PlaceController {

    private final ActivityService activityService;

    // 활동 리스트 조회
    @GetMapping("/activity/{placeId}")
    public ResponseEntity<List<String>> getActivityAll() {
        return ResponseEntity.ok().body(activityService.searchActivityList());
    }
}
