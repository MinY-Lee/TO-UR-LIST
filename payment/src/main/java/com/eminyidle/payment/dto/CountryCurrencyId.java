package com.eminyidle.payment.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CountryCurrencyId implements Serializable {
    @Column(name="currency_code")
    private String currencyCode;
    @Column(name="country_code")
    private String countryCode;
}
