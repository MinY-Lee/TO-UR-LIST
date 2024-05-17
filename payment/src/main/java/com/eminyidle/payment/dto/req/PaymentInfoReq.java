package com.eminyidle.payment.dto.req;

import com.eminyidle.payment.dto.PaymentMember;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentInfoReq {
    @NotBlank
    private String payType;
    @NotBlank
    private String tourId;
    @NotNull @Min(0)
    private Long payAmount;
    @NotNull
    private Double exchangeRate;
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

    private String payerId;

    List<PaymentMember> payMemberList;
 }
