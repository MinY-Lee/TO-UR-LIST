package com.eminyidle.place.place.dto.node;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;
import static org.springframework.data.neo4j.core.schema.Relationship.Direction.OUTGOING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("COUNTRY")
public class Country {

    @Id
    @Property("countryCode")
    private String countryCode;

    @Property("countryName")
    private String countryName;

    @Relationship(type = "IN", direction = INCOMING)
    private City city;

    @Relationship(type = "NEED", direction = OUTGOING)
    private Item item;
}
