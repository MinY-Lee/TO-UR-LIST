package com.eminyidle.place.place.service;

import java.util.List;

public interface ActivityService {

    List<String> searchActivityList();

    void searchTourActivityByPlaceId(String placeId);
}
