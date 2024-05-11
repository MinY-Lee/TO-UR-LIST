package com.eminyidle.feed.adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Place {
    private String placeId;
    private String placeName;
    private Integer tourDay;
    private String tourPlaceId;
    private List<String> activityList;
}
