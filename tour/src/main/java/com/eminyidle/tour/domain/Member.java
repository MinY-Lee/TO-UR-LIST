package com.eminyidle.tour.domain;


import lombok.*;
import org.springframework.data.neo4j.core.schema.Node;

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
