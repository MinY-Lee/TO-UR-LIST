package com.eminyidle.payment.dto.res;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRateRes {
    private String unit;
    private double currencyRate;
    private String currencyCode;
}
