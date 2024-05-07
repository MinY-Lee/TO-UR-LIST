package com.eminyidle.payment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PaymentMember {
    @NotBlank
    private String userId;
    @NotNull
    private Integer payAmount;
}
