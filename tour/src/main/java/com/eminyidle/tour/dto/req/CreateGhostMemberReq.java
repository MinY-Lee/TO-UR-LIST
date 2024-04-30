package com.eminyidle.tour.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGhostMemberReq {
    String tourId;
    String ghostNickname;
}
