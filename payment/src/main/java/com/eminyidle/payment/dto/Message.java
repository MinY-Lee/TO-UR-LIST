package com.eminyidle.payment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {
    @NotBlank
    private String tourId;

    @NotBlank
    private String ghostId;

    @NotBlank
    private String userId;
}
