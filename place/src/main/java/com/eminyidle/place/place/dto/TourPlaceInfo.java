package com.eminyidle.place.place.dto;

import com.eminyidle.place.place.dto.node.TourPlace;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TourPlaceInfo {    // 장소 리스트 조회 시 반환할 객체

    private String placeId;
    private String placeName;
    private Integer tourDay;
    private String tourPlaceId;
    private List<String> tourPlaceList;
//    private List<TourPlace> tourPlaceList;
}
