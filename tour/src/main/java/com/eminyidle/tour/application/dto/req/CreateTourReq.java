package com.eminyidle.tour.application.dto.req;

import com.eminyidle.tour.application.dto.City;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CreateTourReq {
    String tourTitle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<City> cityList;
}
