package com.eminyidle.checklist.adapter.in.messaging.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourPlaceNode {
    String tourId;
    String placeId;
    String placeName;
    Integer tourDay;
    String tourPlaceId;
}
