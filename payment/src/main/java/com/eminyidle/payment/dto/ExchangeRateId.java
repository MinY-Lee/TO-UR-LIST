package com.eminyidle.payment.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ExchangeRateId implements Serializable {
    @Column(name = "currency_code")
    private String currencyCode;
//    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime date;
}
