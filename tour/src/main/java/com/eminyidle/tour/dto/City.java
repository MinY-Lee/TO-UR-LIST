package com.eminyidle.tour.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class City {
    String countryCode;
    String cityName;
}
