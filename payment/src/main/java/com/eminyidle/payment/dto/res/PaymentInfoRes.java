package com.eminyidle.payment.dto.res;

import com.eminyidle.payment.dto.PaymentMember;
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
public class PaymentInfoRes {
    @NotBlank
    private String payId;
    @NotBlank
    private String payType;
    @NotBlank
    private String tourId;
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

    List<PaymentMember> payMemberList;
}
