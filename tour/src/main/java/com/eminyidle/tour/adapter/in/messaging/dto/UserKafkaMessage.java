package com.eminyidle.tour.adapter.in.messaging.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class UserKafkaMessage {
    String type;
    UserNode body;
}
