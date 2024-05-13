package com.eminyidle.tour.application.dto.req;

import com.eminyidle.tour.application.dto.City;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateTourCityReq {
    String tourId;
    List<City> cityList;
}
