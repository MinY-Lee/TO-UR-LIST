package com.eminyidle.tour.application.dto.req;

import com.eminyidle.tour.application.dto.City;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTourCityReq {
    String tourId;
    List<City> cityList;
}
