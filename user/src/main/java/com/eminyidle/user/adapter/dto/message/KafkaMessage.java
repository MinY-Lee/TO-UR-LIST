package com.eminyidle.user.adapter.dto.message;

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
