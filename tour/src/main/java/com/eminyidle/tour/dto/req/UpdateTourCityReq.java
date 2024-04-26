package com.eminyidle.tour.dto.req;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Member;

import java.time.LocalDateTime;
import java.util.List;

public class UpdateTourCityReq {
    String tourId;
    List<City> cityList;
}
