package com.eminyidle.feed.adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class Place {
    private String placeId;
    private String placeName;
    private Integer tourDay;
    private List<String> tourActivityList;
}
