package com.eminyidle.checklist.dto;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

@Node(primaryLabel = "TOUR_ACTIVITY")
public class TourActivity {
    @Id @GeneratedValue
    private String tourActivityId;
    private String activityName;

    @Relationship(type = "REFERENCE")
    private Activity activity;

    @Relationship(type = "DO",direction = Relationship.Direction.INCOMING)
    private TourPlace tourPlace;


}
