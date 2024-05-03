package com.eminyidle.payment.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CountryCurrency {
    @Id
    @Column(name="currency_code")
    private String currencyCode;

    private String countryCode;
    private String currencySign;
}
