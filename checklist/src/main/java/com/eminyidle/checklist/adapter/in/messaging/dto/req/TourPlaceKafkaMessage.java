package com.eminyidle.checklist.adapter.in.messaging.dto.req;

import com.eminyidle.checklist.adapter.in.messaging.dto.TourPlaceNode;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourPlaceKafkaMessage {
    String type;
    TourPlaceNode body;
}
