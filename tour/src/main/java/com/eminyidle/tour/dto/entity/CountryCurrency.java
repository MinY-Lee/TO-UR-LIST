package com.eminyidle.tour.dto.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class CountryCurrency {
    @Id
    @Column(name = "country_code")
    private String countryCode;
    @Column(name = "currency_code")
    private String currencyCode;
    @Column(name = "currency_sign")
    private String currencySign;

}
