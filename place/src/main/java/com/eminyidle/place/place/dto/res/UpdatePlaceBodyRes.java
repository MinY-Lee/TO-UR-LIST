package com.eminyidle.place.place.dto.res;

import com.eminyidle.place.place.dto.TourPlaceInfo;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePlaceBodyRes {
    // 장소 변경사항의 body
    private String tourId;
    private List<TourPlaceInfo> placeList;
}
