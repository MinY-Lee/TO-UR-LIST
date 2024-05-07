package com.eminyidle.tour.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "USER")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class User {
    @Id
    String userId;
    @Property
    String userName;
    @Property
    String userNickname;

    @Relationship(type = "ATTEND", direction = Relationship.Direction.OUTGOING)
    List<Attend> tourList;
}
