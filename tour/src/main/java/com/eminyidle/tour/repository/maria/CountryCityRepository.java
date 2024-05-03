package com.eminyidle.tour.repository.maria;

import com.eminyidle.tour.dto.CityEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CountryCityRepository extends JpaRepository<CityEntity,String> {
    List<CityEntity> findAllByCountryCode(String countryCode);
}
