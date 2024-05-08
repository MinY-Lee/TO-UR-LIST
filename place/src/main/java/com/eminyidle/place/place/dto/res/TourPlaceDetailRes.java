package com.eminyidle.place.place.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPlaceDetailRes {
    private Object placeInfo;
    private List<String> activityList;
    private Boolean isSelected;
}
