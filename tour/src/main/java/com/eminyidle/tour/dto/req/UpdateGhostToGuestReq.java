package com.eminyidle.tour.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UpdateGhostToGuestReq {
    String tourId;
    String ghostId;
    String ghostNickname;
    String userId;
    String userNickname;
}
