package com.eminyidle.tour.country.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CountryInfo {
    String climate;
    String language;
    String currencyUnit ;
    String plug_type;
    String voltage;
    Double KST;
}
