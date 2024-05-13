package com.eminyidle.tour.application.dto.res;

import com.eminyidle.tour.domain.Member;
import com.eminyidle.tour.domain.TourCity;
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
    List<TourCity> cityList;
    List<Member> memberList;
}
