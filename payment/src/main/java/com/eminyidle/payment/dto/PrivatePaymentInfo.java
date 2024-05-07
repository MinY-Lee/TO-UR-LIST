package com.eminyidle.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrivatePaymentInfo {
    @NotBlank
    private String privatePaymentId;

    @NotNull
    private Integer payAmount;

    @NotBlank
    private String unit;

    @NotBlank
    private String currencyCode;

    @NotBlank
    private String payMethod;

    @NotNull
    private LocalDateTime payDatetime;

    @NotNull
    private String payContent;

    @NotBlank
    private String payCategory;
}
