package com.eminyidle.checklist.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "TOUR")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Tour {
    @Id
    private String tourId;
    private Integer tourPeriod;

    @Relationship(type = "MEMBER",direction = Relationship.Direction.OUTGOING)
    private List<User> memberList;
}
