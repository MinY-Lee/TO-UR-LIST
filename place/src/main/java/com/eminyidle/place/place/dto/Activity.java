package com.eminyidle.place.place.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("activity")
public class Activity {

    @Id
    @Property("activity")
    private String activity;
}
