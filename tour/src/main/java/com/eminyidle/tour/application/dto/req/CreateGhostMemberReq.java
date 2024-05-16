package com.eminyidle.tour.application.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateGhostMemberReq {
    String tourId;
    String ghostNickname;
}
