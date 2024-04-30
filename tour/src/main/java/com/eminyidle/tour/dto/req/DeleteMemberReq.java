package com.eminyidle.tour.dto.req;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteMemberReq {
    String tourId;
    String userId;
    String userNickname;
    String memberType;
}
