package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.TourPlace;
import com.eminyidle.place.place.dto.TourPlaceMessageInfo;
import com.eminyidle.place.place.dto.res.SearchPlaceListRes;

import java.util.LinkedHashMap;
import java.util.List;

public interface PlaceService {
    List<SearchPlaceListRes> searchPlaceList(String keyword);


    TourPlaceMessageInfo addPlace(LinkedHashMap<String, Object> body, String tourId);
    List<TourPlace> searchTourPlace(String tourId);
}
