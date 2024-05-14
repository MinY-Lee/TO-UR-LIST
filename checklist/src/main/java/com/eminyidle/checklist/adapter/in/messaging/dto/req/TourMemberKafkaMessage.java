package com.eminyidle.checklist.adapter.in.messaging.dto.req;

import com.eminyidle.checklist.adapter.in.messaging.dto.TourMember;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class TourMemberKafkaMessage {
    String type;
    TourMember body;
}
