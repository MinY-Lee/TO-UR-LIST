package com.eminyidle.tour.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourDetail extends Tour{
    // TODO - 상속과 롬복 잘 되는지 확인!
    //제목, 기간, 도시, 멤버
//    String tourId;
//    String tourTitle;
//    LocalDateTime startDate;
//    LocalDateTime endDate;
//    List<City> cityList;
    List<Member> memberList;
}
