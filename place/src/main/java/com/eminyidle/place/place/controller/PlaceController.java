package com.eminyidle.place.place.controller;

import com.eminyidle.place.place.dto.TourPlace;
import com.eminyidle.place.place.dto.TourPlaceMessageInfo;
import com.eminyidle.place.place.dto.req.TourPlaceReq;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;
import com.eminyidle.place.place.dto.res.TourPlaceRes;
import com.eminyidle.place.place.service.ActivityService;
import com.eminyidle.place.place.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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


    // 장소 상세 정보 조회
//    @GetMapping("/tour/place/{tourId}/{tourDay}/{placeId}")
//    public ResponseEntity


    // 장소 추가
    @MessageMapping("/place/{tourId}")  // 클라이언트에서 보낸 메시지 받을 메서드
    @SendTo("/topic/place/{tourId}")    // 메서드가 처리한 결과 보낼 목적지
    public TourPlaceRes sendMessage(@DestinationVariable("tourId") String tourId,
                                    @Payload TourPlaceReq tourPlaceReq,
                                    @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes) {
//                                    SimpMessageHeaderAccessor headerAccessor) {
        /*
        @DestinationVariable: 메시지의 목적지에서 변수를 추출
        @Payload: 메시지 본문(body)의 내용을 메서드의 인자로 전달할 때 사용
        (클라이언트가 JSON 형태의 메시지를 보냈다면, 이를 GameMessage 객체로 변환하여 메서드에 전달)
        */

        // 초기화
        LinkedHashMap<String, Object> body = tourPlaceReq.getBody();
        Object responseBody = body;
        boolean isSuccess = false;
        TourPlaceMessageInfo tourPlaceMessageInfo;

        // 메시지 타입에 따른 분기
        switch (tourPlaceReq.getType()){
            // 장소 추가
            case ADD_PLACE: {
                tourPlaceMessageInfo = placeService.addPlace(body, tourId, simpSessionAttributes);
                responseBody = tourPlaceMessageInfo.getBody();
                isSuccess = tourPlaceMessageInfo.getIsSuccess();
                break;
            }
        }

        // 바로 반환하지 말고 변수로 받았다가 반환해주기
        return TourPlaceRes.builder()
                .type(tourPlaceReq.getType())
                .isSuccess(isSuccess)
                .body(responseBody)
                .build();
    }

    @GetMapping("/test/{tourActivityId}")
    public void testPlace(@PathVariable String tourActivityId) {
        log.info("장소 리스트 조회");
        activityService.searchTourActivityByPlaceId(tourActivityId);
    }

    @GetMapping("/test/{tourId}/{placeId}")
    public void testSearchPlace(@PathVariable String tourId, @PathVariable String placeId){
        placeService.checkPlaceDuplication(tourId, placeId);
    }
    // 장소 리스트 조회
    @GetMapping("/tour/place/{tourId}")
    public ResponseEntity<List<TourPlace>> searchTourPlace(@PathVariable String tourId) throws IOException {
        return null;
    }
}








