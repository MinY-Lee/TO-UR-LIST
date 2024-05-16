package com.eminyidle.tour.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMemberReq {
    String tourId;
    String userId;
    String userNickname;
    String memberType;
}
