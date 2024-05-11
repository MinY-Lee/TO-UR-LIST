package com.eminyidle.place.place.controller;

import com.eminyidle.place.place.dto.PlaceRequesterInfo;
import com.eminyidle.place.place.dto.TourPlaceInfo;
import com.eminyidle.place.place.dto.TourPlaceMessageInfo;
import com.eminyidle.place.place.dto.req.TourPlaceReq;
import com.eminyidle.place.place.dto.res.*;
import com.eminyidle.place.place.service.ActivityService;
import com.eminyidle.place.place.service.PlaceService;
import com.eminyidle.place.resource.type.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PlaceController {

    private final ActivityService activityService;
    private final PlaceService placeService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 활동 리스트 조회
    @GetMapping("/activity/{placeId}")
    public ResponseEntity<List<String>> getActivityAll() {
        return ResponseEntity.ok().body(activityService.searchActivityList());
    }

    // 장소 검색
    @GetMapping("/place/search/{keyword}")
    public ResponseEntity<List<SearchPlaceListRes>> searchPlaceList(@PathVariable String keyword) throws IOException {
        log.info(keyword);
        return ResponseEntity.ok().body(placeService.searchPlaceList(keyword));
    }

    // 장소 상세 정보 조회
    @GetMapping("/place/{tourId}/{tourDay}/{placeId}")
    public ResponseEntity<TourPlaceDetailRes> searchPlaceDetail(@PathVariable String tourId, @PathVariable Integer tourDay, @PathVariable String placeId) throws IOException {
        log.info("tourId: " + tourId + "    placeId: " + placeId);
        // try - catch 여부에 따라서 return 값을 바꿔주기
        // 서비스에서 오류를 던져주기 때문에 여기서는 리턴값을 다르게 안해줘도 된다
        // 오류가 떠도 200 처리를 해서 빈 객체를 전달해야 하면 여기서 try-catch 처리를 해줄 수도 있다.
        return ResponseEntity.ok().body(placeService.searchPlaceDetail(tourId, tourDay, placeId));
    }

    // 장소 관련 MESSAGE
    @MessageMapping("/place/{tourId}")  // 클라이언트에서 보낸 메시지 받을 메서드
    @SendTo("/topic/place/{tourId}")    // 메서드가 처리한 결과 보낼 목적지
    public UpdatePlaceRes sendMessage(@DestinationVariable("tourId") String tourId,
                                    @Payload TourPlaceReq tourPlaceReq,
                                    @Header("simpSessionAttributes") Map<String, Object> simpSessionAttributes) {
        /*
        @DestinationVariable: 메시지의 목적지에서 변수를 추출
        @Payload: 메시지 본문(body)의 내용을 메서드의 인자로 전달할 때 사용
        (클라이언트가 JSON 형태의 메시지를 보냈다면, 이를 GameMessage 객체로 변환하여 메서드에 전달)
        */

        // 초기화
        LinkedHashMap<String, Object> body = tourPlaceReq.getBody();
        Object responseBody = body;
        Object commonResponse = body;
        UpdatePlaceBodyRes updatePlaceBody = null;
        boolean isSuccess = false;
        TourPlaceMessageInfo tourPlaceMessageInfo;
        String userId = (String) (simpSessionAttributes.get("userId"));


        // 메시지 타입에 따른 분기
        try {
            switch (tourPlaceReq.getType()){
                // 장소 추가
                case ADD_PLACE: {
                    tourPlaceMessageInfo = placeService.addPlace(body, tourId, simpSessionAttributes);
                    responseBody = tourPlaceMessageInfo.getBody();
                    isSuccess = tourPlaceMessageInfo.getIsSuccess();
                    break;
                }
                // 장소 삭제
                case DELETE_PLACE: {
                    tourPlaceMessageInfo = placeService.deletePlace(body, tourId, simpSessionAttributes);
                    responseBody = tourPlaceMessageInfo.getBody();
                    isSuccess = tourPlaceMessageInfo.getIsSuccess();
                    break;
                }
                // 장소 날짜 수정
                case UPDATE_PLACE_DATE: {
                    try {
                        tourPlaceMessageInfo = placeService.updatePlace(body, tourId, simpSessionAttributes);
                        responseBody = tourPlaceMessageInfo.getBody();
                        isSuccess = tourPlaceMessageInfo.getIsSuccess();
                    } catch (Exception e) {
                        log.info("장소 날짜 업데이트 과정에서 오류 발생");
                    }
                    break;
                }
                // 활동 추가
                case ADD_ACTIVITY: {
                    isSuccess = activityService.addActivity(body, tourId, simpSessionAttributes);
                    responseBody = PlaceRequesterInfo.builder()
                            .userId(userId)
                            .build();
                    break;
                }
                // 활동 삭제
                case DELETE_ACTIVITY: {
                    isSuccess = activityService.deleteActivity(body, tourId, simpSessionAttributes);
                    responseBody = PlaceRequesterInfo.builder()
                            .userId(userId)
                            .build();
                    break;
                }
                // 기타 예외처리
                default: {
                    isSuccess = false;
                    responseBody = PlaceRequesterInfo.builder()
                            .userId(userId)
                            .build();
                    break;
                }
            }

//        // 알맞은 response로 반환
//        TourPlaceRes tourPlaceRes = TourPlaceRes.builder()
//                .type(tourPlaceReq.getType())
//                .isSuccess(isSuccess)
//                .body(responseBody)
//                .build();
//        simpMessagingTemplate.convertAndSend("/topic/place/"+tourId, tourPlaceRes);
        updatePlaceBody = null;
        updatePlaceBody = placeService.alertPlaceUpdate(tourId);
        } catch (NoSuchElementException e) {
            isSuccess = false;
            responseBody = PlaceRequesterInfo.builder()
                    .userId(userId)
                    .build();
            log.info("tour 에서 오류 발생");
        }
        // 알맞은 response로 반환
        TourPlaceRes tourPlaceRes = TourPlaceRes.builder()
                .type(tourPlaceReq.getType())
                .isSuccess(isSuccess)
                .body(responseBody)
                .build();
        simpMessagingTemplate.convertAndSend("/topic/place/"+tourId, tourPlaceRes);
        // 장소 변경사항 반환 (모든 메시지에 관해 전송)
        // TODO 없는 tour 구독하면 어떻게 되는지 해주기
        return UpdatePlaceRes.builder()
                .type(MessageType.UPDATE_PLACE)
                .body(updatePlaceBody)
                .build();
    }

    // 장소 리스트 조회
    @GetMapping("/place/{tourId}")
    public ResponseEntity<List<TourPlaceInfo>> searchTourPlace(@PathVariable String tourId){
        log.info("장소 리스트 조회");
        return ResponseEntity.ok().body(placeService.searchTourPlace(tourId));
    }


    // 테스트용 api들
    // tourPlaceId로 연결되어있는 모든 활동 반환
    @GetMapping("/test/{tourPlaceId}")
    public ResponseEntity<List<String>>testPlace(@PathVariable String tourPlaceId) {
        log.info("장소 활동 리스트 조회");
        return ResponseEntity.ok().body(activityService.searchEnrollActivity(tourPlaceId));
    }

    @GetMapping("/test/{tourId}/{tourDay}/{placeId}")
    public void testSearchPlace(@PathVariable String tourId, @PathVariable Integer tourDay, @PathVariable String placeId){
        placeService.checkPlaceDuplication(tourId, tourDay, placeId);
    }
}








