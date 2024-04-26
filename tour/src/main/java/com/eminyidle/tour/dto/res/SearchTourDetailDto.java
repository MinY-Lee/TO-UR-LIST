package com.eminyidle.tour.dto.res;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Member;

import java.time.LocalDateTime;
import java.util.List;

public class SearchTourDetailDto {
    String tourTitle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<City> cityList;
    List<Member> memberList;
}
