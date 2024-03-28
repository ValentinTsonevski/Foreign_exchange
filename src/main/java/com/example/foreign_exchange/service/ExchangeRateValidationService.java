package com.example.foreign_exchange.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExchangeRateValidationService {

    public void validateCurrencyCodes(String baseCurrency, String targetCurrency) {
        List<String> validationErrorMessages = new ArrayList<>();

        if (isCurrencyCodeInvalid(baseCurrency) || isCurrencyCodeInvalid(targetCurrency)) {
            validationErrorMessages.add("You have supplied an invalid Source Currency. [Example: source = USD]");
        }

        if (!validationErrorMessages.isEmpty()) {
            String errorMessage = String.join(". ", validationErrorMessages);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void validateAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("You have supplied an invalid amount.The amount must be positive [Example: amount = 100]");
        }
    }

    public void validateAmount(double amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("You have supplied an invalid amount.The amount must be positive [Example: amount = 100]");
        }
    }


    private boolean isCurrencyCodeInvalid(String currencyCode) {
        return currencyCode == null || currencyCode.trim().isEmpty() || currencyCode.trim().length() != 3;
    }


}
