package com.eminyidle.place.place.repository;

import com.eminyidle.place.place.dto.Activity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityRepository extends Neo4jRepository<Activity, String> {

    List<Activity> findAll();   // 모든 활동 리스트 불러오기
}
