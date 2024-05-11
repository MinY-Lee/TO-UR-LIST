package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.City;
import com.eminyidle.tour.dto.TourDetail;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CityRepository extends Neo4jRepository<City,String> {
    Optional<City> findByCityNameAndCountryCode(String cityName, String countryCode);

    @Query("match(c:CITY {countryCode:$countryCode, cityName: $cityName}) return c")
    Optional<City> findCity(@Param("cityName") String cityName,@Param("countryCode") String countryCode);
    //TODO - 둘 다 같은 코드.. 중 결정

    @Query("MATCH (t:TOUR {tourId: $tourId})-[:TO]->(c:CITY) " +
            "RETURN collect(DISTINCT c) AS cityList")
    List<City> findCitiesByTourId(String tourId);

}
