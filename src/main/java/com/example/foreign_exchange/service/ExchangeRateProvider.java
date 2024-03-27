package com.example.foreign_exchange.service;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface ExchangeRateProvider {
    String getExchangeRate(String baseCurrency, String targetCurrency);
    String getCurrencyConversion(String from, String to, BigDecimal amount);
    String getConversionHistory(String fromCurrency, String toCurrency, double amount, LocalDate transactionDate);
}
