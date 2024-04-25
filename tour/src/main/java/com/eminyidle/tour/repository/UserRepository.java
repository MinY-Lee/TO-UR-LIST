package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.User;
import org.springframework.data.neo4j.repository.Neo4jRepository;

public interface UserRepository extends Neo4jRepository<User, String> {
}
