package com.eminyidle.payment.kafka;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Message {
    private String ghostId;
    private String userId;
}
