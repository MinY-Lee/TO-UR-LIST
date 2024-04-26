package com.eminyidle.tour.dto.req;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Member;
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
