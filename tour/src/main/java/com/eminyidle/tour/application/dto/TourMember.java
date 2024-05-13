package com.eminyidle.tour.application.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourMember {
    String tourId;
    String userId;
    String userNickname;
}
