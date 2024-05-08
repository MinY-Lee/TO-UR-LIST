package com.eminyidle.place.place.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public interface ActivityService {

    List<String> searchActivityList();

    List<String> searchEnrollActivity(String placeId);

    // 활동 추가
    boolean addActivity(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> simpSessionAttributes);

    Boolean checkActivityDuplication(String tourId, String placeId, Integer tourDay, String activity);

    boolean deleteActivity(LinkedHashMap<String, Object> body, String tourId, Map<String, Object> simpSessionAttributes);
}
