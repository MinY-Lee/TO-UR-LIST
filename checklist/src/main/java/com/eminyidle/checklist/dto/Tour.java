package com.eminyidle.checklist.dto;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node(primaryLabel = "TOUR")
public class Tour {
    @Id
    private String tourId;
    private Integer tourPeriod;

    @Relationship(type = "MEMBER")
    private List<User> memberList;
}
