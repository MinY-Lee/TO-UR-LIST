package com.eminyidle.checklist.adapter.in.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TourMember {
    String tourId;
    String userId;
    String userNickname;
}
