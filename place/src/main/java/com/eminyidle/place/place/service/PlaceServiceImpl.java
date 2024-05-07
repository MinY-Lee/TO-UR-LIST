package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.*;
import com.eminyidle.place.place.dto.node.TourPlace;
import com.eminyidle.place.place.dto.res.SearchPlaceDetailRes;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;
import com.eminyidle.place.place.exception.GetRequesterInfoFailException;
import com.eminyidle.place.place.exception.PlaceAddFailException;
import com.eminyidle.place.place.exception.PlaceSearchException;
import com.eminyidle.place.place.repository.DoRelationRepository;
import com.eminyidle.place.place.repository.PlaceRepository;
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
                return responseEntity.getBody().getPlaceList().stream().map(place -> {
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
    public SearchPlaceDetailRes searchPlaceDetail(String tourId, Integer tourDay, String placeId) {
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
                detailUrl,
                HttpMethod.GET,
                requestEntity,
                Place.class
        );
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            // 사진이 없는 경우는 빈 리스트로 대체하여 반환
            if (responseEntity != null) {
                PlaceInfo pLaceInfo = PlaceInfo.builder()
                        .placeId(responseEntity.getBody().getId())
                        .placeName(responseEntity.getBody().getDisplayName().getText())
                        .placePrimaryType(responseEntity.getBody().getPrimaryType())
                        .placeLatitude(responseEntity.getBody().getLocation().getLatitude())
                        .placeLongitude(responseEntity.getBody().getLocation().getLongitude())
                        .placeAddress(responseEntity.getBody().getShortFormattedAddress())
                        .placePhotoList(responseEntity.getBody().getPhotos() == null ? new ArrayList<>() : responseEntity.getBody().getPhotos().stream().map(photo -> photo.getName()).toList())
                        .build();
            }
        } else {
            throw new PlaceSearchException("장소 세부 검색에서 오류");
        }
        return null;
    }

    // 장소 추가
    @Override
    public TourPlaceMessageInfo addPlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers) {
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
        if (checkPlaceDuplication(tourId, newTourDay, placeId)) {
            return TourPlaceMessageInfo.builder()
                    .body(responseBody)
                    .isSuccess(isSuccess)
                    .build();
        }

        try {
            placeRepository.updateTourDay(tourId, placeId, oldTourDay, newTourDay);
//            responseBody = PlaceRequesterInfo.builder()
//                    .userId(userId)
//                    .build();
            isSuccess = true;
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

        return null;
    }
}
