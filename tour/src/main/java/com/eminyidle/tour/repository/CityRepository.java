package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.City;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CityRepository extends Neo4jRepository<City,Long> {
    Optional<City> findByCityNameAndCountryCode(String cityName, String countryCode);

    @Query("match(c:City {countryCode:$countryCode, cityName: $cityName}) return c")
    Optional<City> findCity(@Param("cityName") String cityName,@Param("countryCode") String countryCode);
    //TODO - 둘 다 같은 코드..인데..아래 같은 내용 뜬디.. 알아보잣.. 원하는대로 동작은 함
    /*
    * --- [tour] [nio-8080-exec-2] org.springframework.data.neo4j.cypher    : Executing:
UNWIND $__relationships__ AS relationship WITH relationship MATCH (startNode:`Tour`) WHERE startNode.tourId = relationship.fromId MATCH (endNode) WHERE id(endNode) = relationship.toId MERGE (startNode)-[relProps:`TO`]->(endNode) RETURN elementId(relProps) AS __elementId__
2024-04-25T18:02:25.613+09:00  WARN 16516 --- [tour] [nio-8080-exec-2] o.s.data.neo4j.cypher.deprecation        : Neo.ClientNotification.Statement.FeatureDeprecationWarning: This feature is deprecated and will be removed in future versions.
	UNWIND $__relationships__ AS relationship WITH relationship MATCH (startNode:`Tour`) WHERE startNode.tourId = relationship.fromId MATCH (endNode) WHERE id(endNode) = relationship.toId MERGE (startNode)-[relProps:`TO`]->(endNode) RETURN elementId(relProps) AS __elementId__
	                                                                                                                                                        ^
The query used a deprecated function: `id`.
    * */
}
