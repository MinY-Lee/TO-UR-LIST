package com.eminyidle.place.place.service;

import com.eminyidle.place.place.dto.res.SearchPlaceListRes;

import java.util.List;

public interface PlaceService {
    List<SearchPlaceListRes> searchPlaceList(String keyword);
}
