package com.eminyidle.checklist.application.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Go {
    @RelationshipId @GeneratedValue
    private Long id;

    private String placeId;

    private String placeName;

    private Integer tourDay;

    @TargetNode
    private Tour tour;
}
