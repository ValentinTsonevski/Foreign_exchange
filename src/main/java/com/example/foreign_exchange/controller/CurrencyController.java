package com.example.foreign_exchange.controller;

import com.example.foreign_exchange.service.ExchangeRateProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CurrencyController {

    private final ExchangeRateProvider exchangeRateProvider;

    @GetMapping("/exchange-rates")
    public String getExchangeRates(@RequestParam("baseCurrency") String baseCurrency,
                                   @RequestParam("targetCurrency") String targetCurrency) {
        return exchangeRateProvider.getExchangeRate(baseCurrency, targetCurrency);
    }

    @GetMapping("/conversion")
    public String getCurrencyConversion(@RequestParam("fromCurrency") String fromCurrency,
                                        @RequestParam("toCurrency") String toCurrency,
                                        @RequestParam("amount") BigDecimal amount) {
        return exchangeRateProvider.getCurrencyConversion(fromCurrency, toCurrency, amount);
    }

    @GetMapping("/conversion-history")
    public String getConversionHistory(
            @RequestParam("fromCurrency") String fromCurrency,
            @RequestParam("toCurrency") String toCurrency,
            @RequestParam("amount") double amount,
            @RequestParam("transactionDate") LocalDate transactionDate
    ) {
        return exchangeRateProvider.getConversionHistory(fromCurrency, toCurrency, amount, transactionDate);
    }
}
