package com.eminyidle.checklist.dto;

import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

@Node
public class User {
    @Id
    private String userId;
}
