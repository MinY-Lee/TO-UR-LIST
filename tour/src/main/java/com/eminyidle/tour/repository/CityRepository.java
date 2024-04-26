package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.City;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CityRepository extends Neo4jRepository<City,String> {
    Optional<City> findByCityNameAndCountryCode(String cityName, String countryCode);

    @Query("match(c:City {countryCode:$countryCode, cityName: $cityName}) return c")
    Optional<City> findCity(@Param("cityName") String cityName,@Param("countryCode") String countryCode);
    //TODO - 둘 다 같은 코드..인데..아래 같은 내용 뜬디.. 알아보잣.. 원하는대로 동작은 함

}
