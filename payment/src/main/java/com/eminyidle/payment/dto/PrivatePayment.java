package com.eminyidle.payment.dto;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class PrivatePayment {
    private List<PrivatePaymentInfo> privatePaymentList;

    private List<String> publicPaymentList;
}
