package com.eminyidle.place.place.dto.node;

import com.eminyidle.place.place.dto.node.Country;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import static org.springframework.data.neo4j.core.schema.Relationship.Direction.INCOMING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node("ITEM")
public class Item {

    @Id
    @Property("item")
    private String item;

    @Relationship(type = "NEED", direction = INCOMING)
    private Country country;
}
