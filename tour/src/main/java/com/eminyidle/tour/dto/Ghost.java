package com.eminyidle.tour.dto;

import lombok.*;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Node(primaryLabel = "GHOST")
public class Ghost {
    @Id
    String ghostId;
    @Property
    String ghostNickname;
}
