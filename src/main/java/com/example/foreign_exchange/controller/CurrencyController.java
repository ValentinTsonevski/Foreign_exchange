package com.example.foreign_exchange.controller;

import com.example.foreign_exchange.service.ExchangeRateProvider;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

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
    public String getCurrencyConversion(@RequestParam("from") String from,
                                            @RequestParam("to") String to,
                                            @RequestParam("amount") BigDecimal amount) {
        return exchangeRateProvider.getCurrencyConversion(from, to, amount);
    }
}
