package com.eminyidle.payment.repository;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.CountryCurrencyId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;



@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
// Repository 단위 테스트
class CountryCurrencyRepositoryTest {

    @Autowired
    CountryCurrencyRepository countryCurrencyRepository;

    @Test
    @DisplayName("CurrencyCode로 국가 이름들 찾기 - 성공")
    @Order(1)
    void findByCountryCurrencyIdCurrencyCodeTrueCase() {
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
    @DisplayName("CurrencyCode로 국가 이름들 찾기 - 실패")
    @Order(2)
    void findByCountryCurrencyIdCurrencyCodeFalseCase() {
        //given
        CountryCurrencyId countryCurrencyId = CountryCurrencyId.builder()
                .countryCode("KOR")
                .currencyCode("KRW")
                .build();
        CountryCurrency countryCurrency = CountryCurrency.builder()
                .countryCurrencyId(countryCurrencyId)
                .currencySign("₩")
                .build();
        countryCurrencyRepository.save(countryCurrency);

        // when
        List<CountryCurrency> countryCurrencyList = countryCurrencyRepository.findByCountryCurrencyIdCurrencyCode("KOR");

        // then
        Assertions.assertThat(countryCurrencyList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("CountryCode로 국가 통화코드 찾기 - 성공")
    @Order(3)
    void findByCountryCurrencyIdCountryCodeTrueCase() {
        // given
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
        List<CountryCurrency> countryCurrencyList = countryCurrencyRepository.findByCountryCurrencyIdCountryCode(
                saveCountryCurrency.getCountryCurrencyId().getCountryCode()
        );

        // then
        Assertions.assertThat(countryCurrencyList.size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("CountryCode로 국가 통화코드 찾기 - 실패")
    @Order(4)
    void findByCountryCurrencyIdCountryCodeFalseCase() {
        // given
        CountryCurrencyId countryCurrencyId = CountryCurrencyId.builder()
                .countryCode("KOR")
                .currencyCode("KRW")
                .build();
        CountryCurrency countryCurrency = CountryCurrency.builder()
                .countryCurrencyId(countryCurrencyId)
                .currencySign("₩")
                .build();
        countryCurrencyRepository.save(countryCurrency);

        // when
        List<CountryCurrency> countryCurrencyList = countryCurrencyRepository.findByCountryCurrencyIdCountryCode("KRW");

        // then
        Assertions.assertThat(countryCurrencyList.size()).isEqualTo(0);
    }
}