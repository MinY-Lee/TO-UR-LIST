package com.eminyidle.payment.dto.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayIdReq {
    @NotBlank
    private String tourId;

    @NotBlank
    private String payType;
}
