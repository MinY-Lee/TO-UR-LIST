package com.eminyidle.tour.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.*;

@RelationshipProperties
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Attend {
    @Id
    @GeneratedValue
    Long id;
    @Property
    private String tourTitle;

    @TargetNode
    private Tour tour;
}
