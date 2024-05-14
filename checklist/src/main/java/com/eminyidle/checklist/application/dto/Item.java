package com.eminyidle.checklist.application.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node(primaryLabel = "ITEM")
@Getter
@Setter
@ToString
public class Item {
    @Id
    private String item;
}
