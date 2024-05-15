package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.ExchangeRate;

import java.util.List;

public interface BatchService {
    void saveExchangeRates(String responseBody);

    List<ExchangeRate> loadExchangeRateList();
}
