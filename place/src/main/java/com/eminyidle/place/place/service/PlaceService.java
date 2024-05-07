package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.TourPlaceInfo;
import com.eminyidle.place.place.dto.TourPlaceMessageInfo;
import com.eminyidle.place.place.dto.res.SearchPlaceDetailRes;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface PlaceService {
    List<SearchPlaceListRes> searchPlaceList(String keyword);


    // 장소 세부 검색
    SearchPlaceDetailRes searchPlaceDetail(String tourId, Integer tourDay, String placeId);

    // 장소 추가
    TourPlaceMessageInfo addPlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers);

    // 장소 삭제
    TourPlaceMessageInfo deletePlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> headers);

    // 장소 날짜 수정
    TourPlaceMessageInfo updatePlace(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> simpSessionAttributes);

    // 장소 존재 여부 조회
    Boolean checkPlaceDuplication(String tourId, Integer tourDay, String placeId);

    List<TourPlaceInfo> searchTourPlace(String tourId);

}
