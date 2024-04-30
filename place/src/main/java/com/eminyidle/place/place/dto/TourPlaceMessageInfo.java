package com.eminyidle.place.place.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPlaceMessageInfo {
    private Boolean isSuccess;
    private Object body;
}
