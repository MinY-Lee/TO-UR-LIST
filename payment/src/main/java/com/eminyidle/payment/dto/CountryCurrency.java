package com.eminyidle.payment.dto;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "country_currency")
public class CountryCurrency {
    @EmbeddedId
    private CountryCurrencyId countryCurrencyId;

    private String currencySign;
}
