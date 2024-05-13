package com.eminyidle.checklist.adapter.in.messaging.dto.req;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KafkaMessage {
    String type;
    Object body;
}
