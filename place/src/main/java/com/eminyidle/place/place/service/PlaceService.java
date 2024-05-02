package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.TourPlace;
import com.eminyidle.place.place.dto.TourPlaceMessageInfo;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface PlaceService {
    List<SearchPlaceListRes> searchPlaceList(String keyword);


    // 장소 추가
    TourPlaceMessageInfo addPlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers);

    // 장소 삭제
    TourPlaceMessageInfo deletePlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers);

    // 장소 존재 여부 조회
    Boolean checkPlaceDuplication(String tourId, String placeId);

    List<TourPlace> searchTourPlace(String tourId);

}
