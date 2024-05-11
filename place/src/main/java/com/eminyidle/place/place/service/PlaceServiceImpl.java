package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.*;
import com.eminyidle.place.place.dto.node.Tour;
import com.eminyidle.place.place.dto.node.TourPlace;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;
import com.eminyidle.place.place.dto.res.TourPlaceDetailRes;
import com.eminyidle.place.place.dto.res.UpdatePlaceBodyRes;
import com.eminyidle.place.place.exception.GetRequesterInfoFailException;
import com.eminyidle.place.place.exception.PlaceAddFailException;
import com.eminyidle.place.place.exception.PlaceSearchException;
import com.eminyidle.place.place.repository.DoRelationRepository;
import com.eminyidle.place.place.repository.PlaceRepository;
import com.eminyidle.place.place.repository.TourRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{

    private final RestTemplate restTemplate;

    private final PlaceRepository placeRepository;
    private final DoRelationRepository doRelationRepository;
    private final TourRepository tourRepository;

//    @Value("${spring.googleMap.key}")
//    private String googleMapKey;
    @Value("${PLACE_GOOGLE_API_KEY}")
    private String googleMapKey;

    // 요청하는 기본 Url
    private static final String baseUrl = "https://places.googleapis.com/v1/places:searchText";
    private static final String detailUrl = "https://places.googleapis.com/v1/places/";

    // POST 요청을 통해 장소 검색 결과 받아오기
    @Override
    public List<SearchPlaceListRes> searchPlaceList(String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Json 형식으로 받겠다
        headers.set("X-Goog-Api-Key", googleMapKey);    // 발급받은 Google Api key 설정
        headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.photos," +
                "places.types,places.googleMapsUri,places.primaryType,places.addressComponents," +
                "places.shortFormattedAddress,places.subDestinations,places.location");   // 받아 올 정보
        String requestBody = "{ \"textQuery\" : \"" + keyword + "\" }";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        log.info(requestEntity.toString());
        ResponseEntity<PlaceList> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                requestEntity,
                PlaceList.class
        );

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // 사진이 없는 경우는 빈 리스트로 대체하여 반환
            if(responseEntity != null) {
                return responseEntity.getBody().getPlaces().stream().map(place -> {
                    SearchPlaceListRes searchPlaceRes = SearchPlaceListRes.builder()
                            .placeId(place.getId())
                            .placeName(place.getDisplayName().getText())
                            .placePrimaryType(place.getPrimaryType())
                            .placeLatitude(place.getLocation().getLatitude())
                            .placeLongitude(place.getLocation().getLongitude())
                            .placeAddress(place.getShortFormattedAddress())
                            .placePhotoList(place.getPhotos() == null ? new ArrayList<>() : place.getPhotos().stream().map(photo -> photo.getName()).toList())
                            .build();
                    return searchPlaceRes;
                }).toList();
            }
        }
        // 에러가 발생했거나 응답이 없는 경우 빈 리스트 반환
        log.info(responseEntity.toString());
        return Collections.emptyList();
    }

    // 장소 세부 검색
    @Override
    public TourPlaceDetailRes searchPlaceDetail(String tourId, Integer tourDay, String placeId) {
        String searchUrl = detailUrl + placeId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Json 형식으로 받겠다
        headers.set("X-Goog-Api-Key", googleMapKey);    // 발급받은 Google Api key 설정
        headers.set("X-Goog-FieldMask", "id,displayName,photos," +
                "types,googleMapsUri,primaryType,addressComponents," +
                "shortFormattedAddress,subDestinations,location,,paymentOptions,currentOpeningHours");   // 받아 올 정보

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);
        log.info(requestEntity.toString());
        ResponseEntity<Place> responseEntity = restTemplate.exchange(
                searchUrl,
                HttpMethod.GET,
                requestEntity,
                Place.class
        );
        PlaceInfo placeInfo = null;
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // 사진이 없는 경우는 빈 리스트로 대체하여 반환
            if (responseEntity != null) {
                placeInfo = PlaceInfo.builder()
                        .placeId(responseEntity.getBody().getId())
                        .placeName(responseEntity.getBody().getDisplayName().getText())
                        .placePrimaryType(responseEntity.getBody().getPrimaryType())
                        .placeLatitude(responseEntity.getBody().getLocation().getLatitude())
                        .placeLongitude(responseEntity.getBody().getLocation().getLongitude())
                        .placeAddress(responseEntity.getBody().getShortFormattedAddress())
                        .placeOpenNow(responseEntity.getBody().getCurrentOpeningHours() == null ? null : responseEntity.getBody().getCurrentOpeningHours().getOpenNow())
                        .placeWeekdayDescriptions(responseEntity.getBody().getCurrentOpeningHours() == null ? null : responseEntity.getBody().getCurrentOpeningHours().getWeekdayDescriptions())
                        .placeAcceptCreditCards(responseEntity.getBody().getPaymentOptions() == null ? null : responseEntity.getBody().getPaymentOptions().getAcceptsCreditCards())
                        .placeAcceptCashOnly(responseEntity.getBody().getPaymentOptions() == null ? null : responseEntity.getBody().getPaymentOptions().getAcceptsCashOnly())
                        .placePhotoList(responseEntity.getBody().getPhotos() == null ? new ArrayList<>() : responseEntity.getBody().getPhotos().stream().map(photo -> photo.getName()).toList())
                        .build();
            }
        } else {
            throw new PlaceSearchException("장소 세부 검색에서 오류");
        }
        // tourPlaceId를 찾아서 등록된 활동들 찾아오기
        List<String> tourPlaceActivity = searchTourPlaceActivity(tourId, tourDay, placeId);
        Boolean isSelected = checkPlaceDuplication(tourId, tourDay, placeId);
        return TourPlaceDetailRes.builder()
                .placeInfo(placeInfo)
                .activityList(tourPlaceActivity)
                .isSelected(isSelected)
                .build();
    }

    // 장소 추가
    @Override
    public TourPlaceMessageInfo addPlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers) {
        // 받아 온 body = requestbody
        Object responseBody = body;
        boolean isSuccess = false;
        String userId = (String) (headers.get("userId"));
        String placeId = (String) body.get("placeId");
        Integer tourDay = (Integer) body.get("tourDay");
        try {
            responseBody = PlaceRequesterInfo.builder()
                    .userId(userId)
                    .build();
        } catch (PlaceAddFailException e) {
            log.error(e.getMessage());
        }
        log.info(headers.toString());

        if (checkPlaceDuplication(tourId, tourDay, placeId) == false) {
            TourPlace tourPlace = TourPlace.builder().build();
            try {
                placeRepository.save(tourPlace);
                // DO 관계 생성해주기
                // TourPlace의 Id는 저장된 값을 불러온다
                placeRepository.createDoRelationship((String) body.get("tourId"), UUID.randomUUID().toString(), (String) body.get("placeId"), (String) body.get("placeName"), (Integer) body.get("tourDay"), tourPlace.getTourPlaceId());
                isSuccess = true;
            } catch (Exception e) {
                log.error("{}", e);
            }
        } else {
            isSuccess = false;
        }
//        TourPlace tourActivity = TourPlace.builder().build();
//        try {
//            placeRepository.save(tourActivity);
//            // DO 관계 생성해주기
//            // TourActivity의 Id는 저장된 값을 불러온다
//            placeRepository.createDoRelationship((String) body.get("tourId"), UUID.randomUUID().toString(), (String) body.get("placeId"), (String) body.get("placeName"), (Integer) body.get("tourDay"), tourActivity.getTourActivityId());
//            isSuccess = true;
//        } catch (Exception e) {
//            log.error("{}", e);
//        }



        return TourPlaceMessageInfo.builder()
                .body(responseBody)
                .isSuccess(isSuccess)
                .build();
    }

    // 장소 삭제
    @Override
    public TourPlaceMessageInfo deletePlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers) {
        // 받아 온 body = requestbody
        Object responseBody = body;
        boolean isSuccess = false;
        String userId = (String) (headers.get("userId"));
//        String userName = (String) (headers.get("userName"));
//        String userNickname = (String) (headers.get("userNickname"));
        String placeId = (String) body.get("placeId");
        Integer tourDay = (Integer) body.get("tourDay");
        try {
            responseBody = PlaceRequesterInfo.builder()
                    .userId(userId)
//                    .userNickname(userNickname)
                    .build();
            placeRepository.deletePlaceByTourIdAndPlaceIdAndTourDay(tourId, placeId, tourDay);
            isSuccess = true;
        } catch (GetRequesterInfoFailException e) {
            log.error("{}", e);
        }

        return TourPlaceMessageInfo.builder()
                .body(responseBody)
                .isSuccess(isSuccess)
                .build();
    }

    // 장소 날짜 수정
    @Override
    public TourPlaceMessageInfo updatePlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers) {
        // 받아 온 body = requestbody
        Object responseBody = body;
        boolean isSuccess = false;
        String userId = (String) (headers.get("userId"));
        String placeId = (String) body.get("placeId");
        Integer oldTourDay = (Integer) body.get("oldTourDay");
        Integer newTourDay = (Integer) body.get("newTourDay");
        responseBody = PlaceRequesterInfo.builder()
                .userId(userId)
                .build();
        // 해당하는 날에 해당 장소가 있는지 확인
        if (!checkPlaceDuplication(tourId, oldTourDay, placeId)){
            return TourPlaceMessageInfo.builder()
                    .body(responseBody)
                    .isSuccess(isSuccess)
                    .build();
        }
        // 새로 바꿀 날에 해당 장소가 있는지 확인 -> 있으면 활동 옮겨주고 해당하는 원래 tourPlace를 삭제해주기
//        if (checkPlaceDuplication(tourId, newTourDay, placeId)) {
//            return TourPlaceMessageInfo.builder()
//                    .body(responseBody)
//                    .isSuccess(isSuccess)
//                    .build();
//        }

        try {
            // 새로 바꿀 날에 해당 장소가 있는지 확인 -> 있으면 활동 옮겨주고 해당하는 원래 tourPlace를 삭제해주기
            if (checkPlaceDuplication(tourId, newTourDay, placeId)) {
                log.info("있는 장소에 합치기");
                placeRepository.mergeTourDay(tourId, placeId, oldTourDay, newTourDay);
                isSuccess = true;
            } else {    // 새로 추가하는 경우
                log.info("새로운 장소 생성");
                placeRepository.updateTourDay(tourId, placeId, oldTourDay, newTourDay);
//            responseBody = PlaceRequesterInfo.builder()
//                    .userId(userId)
//                    .build();
                isSuccess = true;
            }
        } catch (Exception e) {
            log.error("{}", e);
        }
        return TourPlaceMessageInfo.builder()
                .body(responseBody)
                .isSuccess(isSuccess)
                .build();
    }

    // 장소 존재 여부 조회
    @Override
    public Boolean checkPlaceDuplication(String tourId, Integer tourDay, String placeId) {
        try {
            // 해당하는 장소가 이미 추가되어 있는 경우
            String ans = doRelationRepository.findPlaceByTourIdAndPlaceId(tourId, tourDay, placeId).orElseThrow(NoSuchElementException::new);
//            Do ans = doRelationRepository.findPlaceByTourIdAndPlaceId(tourId, placeId).orElseThrow(NoSuchElementException::new);
            log.info(ans.toString());
            return true;
        } catch (NoSuchElementException e) {
            log.info("해당하는 장소 없음");
            return false;
        }
    }

    // 장소 리스트 조회
    @Override
    public List<TourPlaceInfo> searchTourPlace(String tourId) {
        // tourId를 받아서 해당 아이디와 DO로 연결된 TourActivity를 전부 가져오기
        // Tour-DO-TourPlace 를 모두 한번에 가져옵니다...
        Tour tour = tourRepository.findById(tourId).orElseThrow(NoSuchElementException::new);
        // Optional 로 받으면 get()이 필요하지만 Tour로 받게 되면 어차피 Tour 자체로 받는 것이기 때문에 get()을 할 필요가 없다.
        log.info(tour.toString());
        try {
            return tour.getPlaceList().stream().map(place -> {
                TourPlaceInfo tourPlaceInfo = TourPlaceInfo.builder()
                        .placeId(place.getPlaceId())
                        .placeName(place.getPlaceName())
                        .tourDay(place.getTourDay())
                        .tourPlaceId(place.getTourPlace().getTourPlaceId())
                        .activityList(place.getTourPlace().getActivityList() == null ? new ArrayList<>() : place.getTourPlace().getActivityList().stream().map(activity -> activity.getActivity()).toList())
                        .build();
                return tourPlaceInfo;
            }).toList();
        } catch (NoSuchElementException e) {
            log.info("해당하는 여행이 없음");
            return null;
        }
    }

    // 장소 변경사항
    @Override
    public UpdatePlaceBodyRes alertPlaceUpdate(String tourId) {
        // Message 전송 시 마다 반환되는 값
        // tourId에 해당하는 장소 리스트 반환
        log.info("장소 변경사항 실행");
        List<TourPlaceInfo> tourPlaceInfo = searchTourPlace(tourId);
        try {
            return UpdatePlaceBodyRes.builder()
                    .tourId(tourId)
                    .placeList(tourPlaceInfo)
                    .build();
        } catch (NoSuchElementException e) {
            log.info("해당하는 여행이 없음");
            return null;
        }
    }

    // 장소 상세 정보 조회 - 활동 리스트 조회
    @Override
    public List<String> searchTourPlaceActivity(String tourId, Integer tourDay, String placeId) {
        log.info("장소 상세 정보 조회 - 활동 리스트 조회");
        List<String> enrollActivity = placeRepository.findActivityByTourIdAndTourDayAndTourPlaceId(tourId, tourDay, placeId);
        return enrollActivity;
    }
}
