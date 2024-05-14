package com.eminyidle.checklist.adapter.in.messaging.dto.req;

import com.eminyidle.checklist.adapter.in.messaging.dto.TourNode;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourKafkaMessage {
    private String type;
    private TourNode body;
    private String desc;
}
