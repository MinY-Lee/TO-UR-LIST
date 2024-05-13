package com.eminyidle.checklist.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@RelationshipProperties
public class Take {
    @RelationshipId
    Long id;
    String type;
    String userId;
    Boolean isChecked;
    LocalDateTime createdAt;

    @TargetNode
    Item item;
}
