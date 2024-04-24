package com.eminyidle.tour.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Country {
    String countryCode;
    String countryName;
}
