package com.eminyidle.payment.repository;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.CountryCurrencyId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;



@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Repository 단위 테스트
class CountryCurrencyRepositoryTest {

    @Autowired
    CountryCurrencyRepository countryCurrencyRepository;

    @Test
    @DisplayName("CurrencyCode로 국가 이름들 찾기")
    void findByCountryCurrencyIdCurrencyCode() {
        //given
        CountryCurrencyId countryCurrencyId = CountryCurrencyId.builder()
                .countryCode("KOR")
                .currencyCode("KRW")
                .build();
        CountryCurrency countryCurrency = CountryCurrency.builder()
                .countryCurrencyId(countryCurrencyId)
                .currencySign("₩")
                .build();
        CountryCurrency saveCountryCurrency = countryCurrencyRepository.save(countryCurrency);

        // when
        List<CountryCurrency> countryCurrencyList = countryCurrencyRepository.findByCountryCurrencyIdCurrencyCode(
                saveCountryCurrency.getCountryCurrencyId().getCurrencyCode()
        );

        // then
        Assertions.assertThat(countryCurrencyList.size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("CountryCode로 국가 통화코드 찾기")
    void findByCountryCurrencyIdCountryCode() {
    }
}