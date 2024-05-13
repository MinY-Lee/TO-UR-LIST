package com.eminyidle.tour.country.repository;

import com.eminyidle.tour.country.dto.entity.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity,String> {
}
