package com.example.foreign_exchange.service;

import java.math.BigDecimal;

public interface ExchangeRateProvider {
    String getExchangeRate(String baseCurrency, String targetCurrency);
    String getCurrencyConversion(String from, String to, BigDecimal amount);
}
