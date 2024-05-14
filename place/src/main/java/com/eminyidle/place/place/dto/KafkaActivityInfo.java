package com.eminyidle.place.place.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class KafkaActivityInfo {
    private String tourId;
    private String placeId;
    private Integer tourDay;
    private String activity;
    private String tourPlaceId;
}
