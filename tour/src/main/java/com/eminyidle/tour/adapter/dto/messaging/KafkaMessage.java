package com.eminyidle.tour.adapter.dto.messaging;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KafkaMessage {
    String type;
    Object body;
}
