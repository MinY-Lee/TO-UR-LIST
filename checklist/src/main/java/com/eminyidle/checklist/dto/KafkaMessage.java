package com.eminyidle.checklist.dto;

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
