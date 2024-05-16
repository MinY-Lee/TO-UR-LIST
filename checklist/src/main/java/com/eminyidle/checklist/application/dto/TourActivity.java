package com.eminyidle.checklist.application.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node(primaryLabel = "TOUR_ACTIVITY")
@Getter
@Setter
@Builder
public class TourActivity {
    @Id
    private String tourActivityId;
    private String activity;

    @Relationship(type = "REFERENCE")
    private Activity activityNode;

    @Relationship(type = "DO",direction = Relationship.Direction.INCOMING)
    private TourPlace tourPlace;


}
