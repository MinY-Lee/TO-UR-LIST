package com.eminyidle.feed.adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Tour {
    private String tourTitle;
    private List<City> cityList;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
