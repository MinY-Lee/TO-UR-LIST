package com.eminyidle.place.place.dto;

import com.eminyidle.place.resource.type.MessageType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPlaceMessage {
    private MessageType type;
    private Object body;
}
