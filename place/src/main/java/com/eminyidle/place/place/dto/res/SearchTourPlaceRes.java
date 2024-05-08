package com.eminyidle.place.place.dto.res;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchTourPlaceRes {
    private String placeId;
    private String placeName;
    private Integer tourDay;
    private String tourPlaceId;
    private List<String> activityList;
}
