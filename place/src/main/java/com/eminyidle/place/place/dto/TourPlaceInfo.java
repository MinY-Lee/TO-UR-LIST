package com.eminyidle.place.place.dto;

import com.eminyidle.place.place.dto.node.TourPlace;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourPlaceInfo {
    // 장소 리스트 조회 시 반환할 객체
    private String placeId;
    private String placeName;
    private Integer tourDay;
    private String tourPlaceId;
    private List<String> activityList;
}
