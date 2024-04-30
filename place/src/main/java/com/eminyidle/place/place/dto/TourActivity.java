package com.eminyidle.place.place.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourActivity {
    @Id
    @GeneratedValue
    private String tourActivityId;

    @Relationship(type = "IN", direction = OUTGOING)
    private Activity activity;
}
