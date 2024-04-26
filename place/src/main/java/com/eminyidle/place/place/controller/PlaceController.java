package com.eminyidle.place.place.controller;

import com.eminyidle.place.place.dto.Activity;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;
import com.eminyidle.place.place.service.ActivityService;
import com.eminyidle.place.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PlaceController {

    private final ActivityService activityService;
    private final PlaceService placeService;

    // 활동 리스트 조회
    @GetMapping("/activity/{placeId}")
    public ResponseEntity<List<String>> getActivityAll() {
        return ResponseEntity.ok().body(activityService.searchActivityList());
    }

    // 장소 검색
    @GetMapping("/tour/place/search/{keyword}")
    public ResponseEntity<List<SearchPlaceListRes>> searchPlaceList(@PathVariable String keyword) throws IOException {
        log.info(keyword);
        return ResponseEntity.ok().body(placeService.searchPlaceList(keyword));
    }
}
