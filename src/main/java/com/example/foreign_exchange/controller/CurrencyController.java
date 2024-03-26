package com.example.foreign_exchange.controller;

import com.example.foreign_exchange.service.CurrencyService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class CurrencyController {

    private final CurrencyService currencyService;


    @GetMapping("/exchange-rates")
    public String getExchangeRates(@RequestParam("baseCurrency") String baseCurrency, @RequestParam("targetCurrency") String targetCurrency) {
        return currencyService.getExchangeRate(baseCurrency, targetCurrency);
    }
}
