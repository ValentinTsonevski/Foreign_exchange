package com.example.foreign_exchange.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class ExchangeRateValidationServiceTest {

    private ExchangeRateValidationService service;

    @BeforeEach
    void setUp() {
        service = new ExchangeRateValidationService();
    }

    @Test
    void validateCurrencyCodesSuccessTest() {
        assertDoesNotThrow(() -> service.validateCurrencyCodes("USD", "EUR"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCurrencyCodes")
    public void validateCurrencyCodesWithInvalidParamsTest(String sourceCurrency, String targetCurrency, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> service.validateCurrencyCodes(sourceCurrency, targetCurrency));

        assertTrue(exception.getMessage().contains(expectedErrorMessage));
    }

    @Test
    void validateAmountBigDecimalWithPositiveAmountTest() {
        assertDoesNotThrow(() -> service.validateAmount(BigDecimal.valueOf(100)));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidAmounts")
    public void validateAmountWithInvalidParamsTest(BigDecimal amount, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.validateAmount(amount));

        assertTrue(exception.getMessage().contains(expectedErrorMessage));
    }

    @Test
    void validateAmountDoubleWithPositiveAmountTest() {
        assertDoesNotThrow(() -> service.validateAmount(100.0));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidDoubleAmounts")
    public void validateDoubleAmountWithInvalidParamsTest(Double amount, String expectedErrorMessage) {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> service.validateAmount(amount));

        assertTrue(exception.getMessage().contains(expectedErrorMessage));
    }

    private static Stream<Arguments> provideInvalidCurrencyCodes() {
        return Stream.of(arguments("invalid", "EUR", "invalid Source Currency"),
                arguments("USD", "invalid", "invalid Source Currency"),
                arguments(null, null, "invalid Source Currency"));
    }

    private static Stream<Arguments> provideInvalidAmounts() {
        return Stream.of(arguments(BigDecimal.ZERO, "invalid amount"),
                arguments(BigDecimal.valueOf(-100), "invalid amount"));
    }

    private static Stream<Arguments> provideInvalidDoubleAmounts() {
        return Stream.of(arguments(0.0, "invalid amount"), arguments(-100.0, "invalid amount"));
    }
}
