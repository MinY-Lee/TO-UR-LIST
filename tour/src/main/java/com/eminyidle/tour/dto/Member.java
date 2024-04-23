package com.eminyidle.tour.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
    String userId;
    String userNickname;
    String userName;
    String memberType; //host, guest, ghost
}
