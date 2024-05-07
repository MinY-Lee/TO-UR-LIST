package com.eminyidle.payment.dto;

import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document
public class PaymentInfo {
    @Id
    private String id;

    @Field("publicPayment")
    private Map<String,PublicPayment> publicPayment;

    @Field("privatePayment")
    private Map<String,PrivatePayment> privatePayment;
}
