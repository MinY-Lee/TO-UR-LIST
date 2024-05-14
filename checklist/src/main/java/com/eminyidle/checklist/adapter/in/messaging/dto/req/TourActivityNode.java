package com.eminyidle.checklist.adapter.in.messaging.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TourActivityNode {
    private String tourId;
    private String placeId;
    private Integer tourDay;
    private String activity;
    private String tourPlaceId;
}
