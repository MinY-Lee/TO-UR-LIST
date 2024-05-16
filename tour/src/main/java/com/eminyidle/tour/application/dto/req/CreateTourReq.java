package com.eminyidle.tour.application.dto.req;

import com.eminyidle.tour.application.dto.City;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateTourReq {
    String tourTitle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<City> cityList;
}
