package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.Place;
import com.eminyidle.place.place.dto.Places;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.MergedAnnotations;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService{

    private final RestTemplate restTemplate;

//    @Value("${spring.googleMap.key}")
//    private String googleMapKey;
    private String googleMapKey = "AIzaSyBjN4O5KFlbNe8GayyZDNy4WRaeNFUH3mY";

    // 요청하는 기본 Url
    private static final String baseUrl = "https://places.googleapis.com/v1/places:searchText";

    // POST 요청을 통해 장소 검색 결과 받아오기
    @Override
    public List<SearchPlaceListRes> searchPlaceList(String keyword) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON); // Json 형식으로 받겠다
        headers.set("X-Goog-Api-Key", googleMapKey);    // 발급받은 Google Api key 설정
        headers.set("X-Goog-FieldMask", "places.id,places.displayName,places.photos," +
                "places.types,places.googleMapsUri,places.primaryType,places.addressComponents");   // 받아 올 정보
        String requestBody = "{ \"textQuery\" : \"keyword\" }";

//        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        Sample sample = new Sample();
        sample.textQuery = keyword;
        HttpEntity<Sample> requestEntity = new HttpEntity<>(sample, headers);
        log.info(requestEntity.toString());
        ResponseEntity<Places> responseEntity = restTemplate.exchange(
                baseUrl,
                HttpMethod.POST,
                requestEntity,
                Places.class
        );
        log.info(responseEntity.getStatusCode().toString());

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            log.info(responseEntity.toString());
//            List<Places> places = responseEntity.getBody();
//            if (places != null) {
//                return places;
//            }
            if(responseEntity != null) {
                return responseEntity.getBody().getPlaces().stream().map(place -> {
                    SearchPlaceListRes searchPlaceRes = SearchPlaceListRes.builder()
                            .placeId(place.getId())
                            .placeName(place.getDisplayName().getText())
                            .placePhotoList(place.getPhotos().stream().map(photo -> photo.getName()).toList())
                            .build();
                    return searchPlaceRes;
                }).toList();
            }
        }
        // 에러가 발생했거나 응답이 없는 경우 빈 리스트 반환
        log.info(responseEntity.toString());
        return Collections.emptyList();
    }
//    @Override
//    public List<SearchPlaceListRes> searchPlaceList() {
//
//
//        return null;
//    }

    @Setter
    @Getter
    @NoArgsConstructor
    public class Sample{
        private String textQuery;
    }
}
