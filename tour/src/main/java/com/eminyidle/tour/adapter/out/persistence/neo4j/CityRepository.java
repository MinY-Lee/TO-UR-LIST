package com.eminyidle.tour.adapter.out.persistence.neo4j;

import com.eminyidle.tour.application.dto.City;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends Neo4jRepository<City,String> {
    Optional<City> findByCityNameAndCountryCode(String cityName, String countryCode);

    @Query("MATCH (t:TOUR {tourId: $tourId})-[:TO]->(c:CITY) " +
            "RETURN collect(DISTINCT c) AS cityList")
    List<City> findCitiesByTourId(String tourId);

}
