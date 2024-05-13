package com.eminyidle.tour.application.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTourTitleReq {
    String tourId;
    String tourTitle;
}
