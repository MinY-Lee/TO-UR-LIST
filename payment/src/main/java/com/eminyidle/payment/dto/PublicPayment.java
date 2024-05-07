package com.eminyidle.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PublicPayment {
//    @NotBlank
//    private String publicPaymentId;

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

    @NotBlank
    private String payerId;

    @NotNull
    private List<PaymentMember> payMemberList;
}
