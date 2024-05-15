package com.eminyidle.checklist.adapter.in.messaging.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class TourNode {
    //제목, 기간, 도시
    String tourId;
    String tourTitle;
    List<City> cityList;

    LocalDateTime startDate;

    LocalDateTime endDate;

}
