package com.eminyidle.payment.dto;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "exchange_rate")
public class ExchangeRate {
    @EmbeddedId
    private ExchangeRateId exchangeRateId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumns({
//            @JoinColumn(name="currency_code", referencedColumnName="currency_code", insertable=false, updatable=false),
//            @JoinColumn(name="country_code", referencedColumnName="country_code", insertable=false, updatable=false)
//    })
//    private CountryCurrency countryCurrency;

    private Double exchangeRate;  // 필드 이름을 카멜케이스로 변경
}
