package com.eminyidle.place.place.dto.node;

import com.eminyidle.place.place.dto.Do;
import com.eminyidle.place.place.dto.node.Activity;
import com.eminyidle.place.place.dto.node.Tour;
import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.UUID;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node("TOUR_ACTIVITY")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TourActivity {
    @Id
    @Builder.Default
    private String tourActivityId = UUID.randomUUID().toString();
//    @Property
//    private String tourActivityName;

    @Relationship(type = "REFERENCE", direction = OUTGOING)
    private Activity activity;

    @Relationship(type = "DO", direction = INCOMING)
    private Tour tour;

    @Relationship(type = "DO", direction = INCOMING)
    private Do placeInfo;
}
