package com.eminyidle.feed.adapter.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Item {
    private String placeId;
    private String activity;
    private Integer tourDay;
    private String item;
}
