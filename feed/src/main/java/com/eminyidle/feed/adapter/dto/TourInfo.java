package com.eminyidle.feed.adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class TourInfo {
    private String userNickname;
    private List<String> feedThemeTagList;
    private String feedMateTag;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private List<City> cityList;
    private List<Place> placeList;
    private List<Item> itemList;
}
