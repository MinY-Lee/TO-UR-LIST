package com.eminyidle.tour.repository.maria;

import com.eminyidle.tour.dto.entity.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CountryCityRepository extends JpaRepository<CityEntity,String> {
    List<CityEntity> findAllByCountryCode(String countryCode);

    Optional<CityEntity> findCityEntityByCountryCodeAndCityNameKor(String countryCode, String cityNameKor);
}
