package com.example.foreign_exchange.service;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrencyLayerProviderServiceTest {

    @Mock
    private ExchangeRateValidationService validationService;
    @InjectMocks
    private CurrencyLayerProviderService service;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void exchangeRateSuccessTest() {
        String result = service.getExchangeRate("USD", "EUR");
        assertTrue(result.contains("Currency Exchange Rate from USD to EUR"));
    }

    @Test
    public void exchangeRateInvalidCurrencyTest() {
        assertThrows(IllegalArgumentException.class, () -> service.getExchangeRate("USD", "asd"));
    }

    @Test
    public void currencyConversionSuccessTest() {
        String result = service.getCurrencyConversion("USD", "EUR", BigDecimal.valueOf(100.0));
        assertTrue(result.contains("Unique transaction identifier"));
    }

    @Test
    public void currencyConversionInvalidCurrencyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getCurrencyConversion("USD", "asd", BigDecimal.valueOf(100)));
    }

    @Test
    public void currencyConversionInvalidAmountTest() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getCurrencyConversion("USD", "EUR", BigDecimal.valueOf(-5)));
    }

    @Test
    public void conversionHistorySuccessTest() {
        String result = service.getConversionHistory("USD", "EUR", 100.0,
                LocalDate.of(2022,10,20));

        assertTrue(result.contains("Conversion from USD to EUR"));
    }

    @Test
    public void conversionHistoryInvalidCurrencyTest() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getConversionHistory("USD", "asd", 100.0,
                        LocalDate.of(2014,10,20)));
    }

    @Test
    public void conversionHistoryInvalidAmountTest() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getConversionHistory("USD", "EUR", -5.0,
                        LocalDate.of(2014,10,20)));
    }

    @Test
    public void conversionHistoryInvalidDateTest() {
        assertThrows(IllegalArgumentException.class,
                () -> service.getConversionHistory("USD", "EUR", 100.0,
                        LocalDate.of(2064,10,20)));
    }
}




