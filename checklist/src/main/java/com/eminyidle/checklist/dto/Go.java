package com.eminyidle.checklist.dto;

import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
public class Go {
    @RelationshipId
    private Long id;

    private String placeId;

    private String placeName;

    private Integer tourDay;

    @TargetNode
    private Tour tour;
}
