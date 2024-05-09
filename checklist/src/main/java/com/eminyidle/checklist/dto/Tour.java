package com.eminyidle.checklist.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "TOUR")
@Getter
@Setter
public class Tour {
    @Id
    private String tourId;
    private Integer tourPeriod;

    @Relationship(type = "MEMBER")
    private List<User> memberList;
}
