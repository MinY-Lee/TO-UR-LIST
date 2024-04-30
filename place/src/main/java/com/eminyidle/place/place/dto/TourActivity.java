package com.eminyidle.place.place.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.UUID;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Node
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TourActivity {
    @Id
    @Builder.Default
    private String tourActivityId = UUID.randomUUID().toString();

    @Relationship(type = "REFERENCE", direction = OUTGOING)
    private Activity activity;

    @Relationship(type = "DO", direction = INCOMING)
    private Tour tour;
}
