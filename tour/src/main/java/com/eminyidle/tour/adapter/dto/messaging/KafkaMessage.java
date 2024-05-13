package com.eminyidle.tour.adapter.dto.messaging;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class KafkaMessage {
    String type;
    Object body;
}
