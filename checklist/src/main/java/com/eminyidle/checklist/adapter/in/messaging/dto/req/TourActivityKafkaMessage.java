package com.eminyidle.checklist.adapter.in.messaging.dto.req;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourActivityKafkaMessage {
    String type;
    TourActivityNode body;
}
