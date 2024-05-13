package com.eminyidle.place.place.dto;

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
