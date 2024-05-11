package com.eminyidle.place.place.dto.node;

import com.eminyidle.place.place.dto.Do;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;
import java.util.UUID;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("TOUR_PLACE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TourPlace {
    @Id
    @Builder.Default
    private String tourPlaceId = UUID.randomUUID().toString();
//    @Property
//    private String tourActivityName;

    @Relationship(type = "REFERENCE", direction = OUTGOING)
    private List<Activity> activityList;

//    @Relationship(type = "DO", direction = INCOMING)
//    private Tour tour;

    @Relationship(type = "DO", direction = INCOMING)
    private Do placeInfo;
}
