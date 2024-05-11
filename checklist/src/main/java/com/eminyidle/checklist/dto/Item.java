package com.eminyidle.checklist.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node(primaryLabel = "ITEM")
@Getter
@Setter
public class Item {
    @Id
    private String item;
}
