package com.eminyidle.payment.repository;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.CountryCurrencyId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency, CountryCurrencyId> {
    // 복합 키의 일부인 countryCode를 사용하여 조회
    List<CountryCurrency> findByCountryCurrencyIdCurrencyCode(String currencyCode);

    List<CountryCurrency> findByCountryCurrencyIdCountryCode(String countryCode);
}
