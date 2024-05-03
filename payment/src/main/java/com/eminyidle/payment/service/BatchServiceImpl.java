package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.ExchangeRate;
import com.eminyidle.payment.dto.ExchangeRateId;
import com.eminyidle.payment.repository.CountryCurrencyRepository;
import com.eminyidle.payment.repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService{

    private final CountryCurrencyRepository countryCurrencyRepository;
    private final ExchangeRateRepository exchangeRateRepository;
    @Override
    public void saveExchangeRates(String responseBody) {
        // JSON 파싱
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONObject conversionRates = jsonObject.getJSONObject("conversion_rates");

        conversionRates.keys().forEachRemaining(key -> {
            // 11시 05분 기준
            LocalDateTime now = LocalDateTime.now();
            now = now.withHour(11).withMinute(5).withSecond(0);

            // key가 DB에 있는지 확인
            Optional<CountryCurrency> countryCurrency = countryCurrencyRepository.findById(key);
            if (!countryCurrency.isEmpty()) {
                double rate = conversionRates.getDouble(key);

                ExchangeRate exchangeRate = ExchangeRate.builder()
                        .exchangeRateId(new ExchangeRateId(countryCurrency.get().getCurrencyCode(), now))
                        .countryCurrency(countryCurrency.get())
                        .exchangeRate(rate)
                        .build();
                exchangeRateRepository.save(exchangeRate);
            }
        });
    }
}
