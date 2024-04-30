package com.eminyidle.place.place.dto.res;

import com.eminyidle.place.resource.type.MessageType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TourPlaceRes {
    private MessageType type;
    private Boolean isSuccess;
    private Object body;
}
