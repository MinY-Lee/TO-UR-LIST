package com.eminyidle.place.place.dto.node;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("ACTIVITY")
@Builder
@ToString
public class Activity {

    @Id
    @Property("activity")
    private String activity;
}
