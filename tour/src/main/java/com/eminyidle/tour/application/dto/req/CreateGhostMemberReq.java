package com.eminyidle.tour.application.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateGhostMemberReq {
    String tourId;
    String ghostNickname;
}
