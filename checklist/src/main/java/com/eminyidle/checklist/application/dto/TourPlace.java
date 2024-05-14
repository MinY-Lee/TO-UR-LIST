package com.eminyidle.checklist.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node(primaryLabel = "TOUR_PLACE")
@Getter
@Setter
@Builder
public class TourPlace {
    @Id
    private String tourPlaceId;

    @Relationship(type = "GO", direction = Relationship.Direction.INCOMING)
    private Go placeAndTour;
}
