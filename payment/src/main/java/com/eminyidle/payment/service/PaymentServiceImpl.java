package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.ExchangeRateId;
import com.eminyidle.payment.exception.CurrencyNotExistException;
import com.eminyidle.payment.exception.ExchangeRateNotExistException;
import com.eminyidle.payment.repository.CountryCurrencyRepository;
import com.eminyidle.payment.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final CountryCurrencyRepository countryCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    // 통화코드 조회
    @Override
    public ExchangeRate loadExchangeRate(String countryCode, String date) {

        // 통화코드 찾기
        CountryCurrency countryCurrency = countryCurrencyRepository.findByCountryCode(countryCode)
                .orElseThrow(() -> new CurrencyNotExistException("해당하는 통화코드가 없습니다."));

        // 날짜 정보
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        LocalDate datePart = LocalDate.parse(date, formatter);
        LocalDateTime dateTime = datePart.atTime(11, 5, 0);
        // 환율 가져오기
        return exchangeRateRepository.findByExchangeRateId(new ExchangeRateId(countryCurrency.getCurrencyCode(), dateTime))
                .orElseThrow(() -> new ExchangeRateNotExistException("해당하는 환율데이터가 없습니다."));
    }
}
