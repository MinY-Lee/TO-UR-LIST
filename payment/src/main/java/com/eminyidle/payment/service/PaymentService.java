package com.eminyidle.payment.service;

import com.eminyidle.payment.dto.CountryCurrency;
import com.eminyidle.payment.dto.ExchangeRate;

public interface PaymentService {
    ExchangeRate loadExchangeRate(String countryCode, String date);

    CountryCurrency loadCountryCurrency(String countryCode);
}
