package com.eminyidle.place.place.dto.req;

import com.eminyidle.place.resource.type.MessageType;
import lombok.*;

import java.util.LinkedHashMap;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPlaceReq {
    private MessageType type;
    // 메시지 타입마다 생긴게 달라서...
    private LinkedHashMap<String, Object> body;
}
