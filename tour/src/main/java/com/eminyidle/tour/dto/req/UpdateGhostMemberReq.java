package com.eminyidle.tour.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateGhostMemberReq {
    String tourId;
    String ghostId;
    String ghostNickname;
}
