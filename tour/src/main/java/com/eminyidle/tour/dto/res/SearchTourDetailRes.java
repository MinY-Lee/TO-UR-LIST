package com.eminyidle.tour.dto.res;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.Member;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTourDetailRes {
    String tourTitle;
    LocalDateTime startDate;
    LocalDateTime endDate;
    List<City> cityList;
    List<Member> memberList;
}
