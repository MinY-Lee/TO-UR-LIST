package com.eminyidle.tour.repository;

import com.eminyidle.tour.dto.CountryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryRepository extends JpaRepository<CountryEntity,String> {
}
