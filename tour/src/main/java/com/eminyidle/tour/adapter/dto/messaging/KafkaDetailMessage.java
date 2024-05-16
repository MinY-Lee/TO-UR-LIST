package com.eminyidle.tour.adapter.dto.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaDetailMessage extends KafkaMessage{

    private String detail;
    public KafkaDetailMessage() {
        super();
    }
    public KafkaDetailMessage(String type, Object body) {
        super(type, body);
    }
    public KafkaDetailMessage(String type, Object body, String detail) {
        super(type, body);
        setDetail(detail);
    }
}
