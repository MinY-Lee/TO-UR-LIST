package com.eminyidle.tour.application.dto.req;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class UpdateTourPeriodReq {
    String tourId;
    LocalDateTime startDate;
    LocalDateTime endDate;
}
