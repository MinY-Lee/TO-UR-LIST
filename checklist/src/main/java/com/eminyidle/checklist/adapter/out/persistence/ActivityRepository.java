package com.eminyidle.checklist.adapter.out.persistence;

import com.eminyidle.checklist.application.dto.Activity;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface ActivityRepository extends Neo4jRepository<Activity,String> {
}
