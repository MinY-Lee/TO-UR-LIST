package com.eminyidle.tour.dto.res;

import com.eminyidle.tour.dto.TourCity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class SearchTourRes {

    String tourId;
    String tourTitle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<TourCity> cityList;
}
