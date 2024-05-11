package com.eminyidle.place.place.dto.res;

import com.eminyidle.place.resource.type.MessageType;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePlaceRes {
    private MessageType type;
    private Object body;
}
