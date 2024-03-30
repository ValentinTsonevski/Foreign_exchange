package com.example.foreign_exchange.controller;

import com.example.foreign_exchange.service.ExchangeRateProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ExchangeRateProvider exchangeRateProvider;
    private LocalDate SAMPLE_DATE;

    @BeforeEach
    public void setup() {
        SAMPLE_DATE = LocalDate.now().minusDays(2);
    }

    @Test
    public void getExchangeRatesSuccessTest() throws Exception {
        String expectedResponse = "success";

        when(exchangeRateProvider.getExchangeRate("USD", "EUR"))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/exchange-rates")
                        .param("baseCurrency", "USD")
                        .param("targetCurrency", "EUR"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(exchangeRateProvider).getExchangeRate("USD", "EUR");
    }

    @Test
    public void exchangeRatesWithInvalidCurrencyTest() throws Exception {
        when(exchangeRateProvider.getExchangeRate(anyString(), anyString()))
                .thenThrow(new IllegalArgumentException("Invalid currency"));

        mockMvc.perform(get("/api/exchange-rates")
                        .param("baseCurrency", "USD")
                        .param("targetCurrency", "invalid"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid currency"));

        verify(exchangeRateProvider).getExchangeRate(anyString(), anyString());
    }

    @Test
    public void currencyConversionSuccessTest() throws Exception {
        String expectedResponse = "success";

        when(exchangeRateProvider.getCurrencyConversion("USD", "EUR", new BigDecimal("10")))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/conversion")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "EUR")
                        .param("amount", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(exchangeRateProvider).getCurrencyConversion("USD", "EUR", new BigDecimal("10"));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCurrencyConversionParams")
    public void currencyConversionWithInvalidParamsTest(String fromCurrency, String toCurrency, String amount, String expectedErrorMessage) throws Exception {
        when(exchangeRateProvider.getCurrencyConversion(anyString(), anyString(), any()))

                .thenThrow(new IllegalArgumentException(expectedErrorMessage));

        mockMvc.perform(get("/api/conversion")
                        .param("fromCurrency", fromCurrency)
                        .param("toCurrency", toCurrency)
                        .param("amount", amount))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));

        verify(exchangeRateProvider).getCurrencyConversion(anyString(), anyString(), any());
    }

    @Test
    public void conversionHistorySuccessTest() throws Exception {
        String expectedResponse = "success";

        when(exchangeRateProvider.getConversionHistory("USD", "EUR", 1.0, SAMPLE_DATE))
                .thenReturn(expectedResponse);

        mockMvc.perform(get("/api/conversion-history")
                        .param("fromCurrency", "USD")
                        .param("toCurrency", "EUR")
                        .param("amount", "1.0")
                        .param("transactionDate", SAMPLE_DATE.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedResponse));

        verify(exchangeRateProvider).getConversionHistory("USD", "EUR", 1.0, SAMPLE_DATE);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidConversionHistoryParams")
    public void conversionHistoryWithInvalidParamsTest(String fromCurrency, String toCurrency, String amount, LocalDate transactionDate, String expectedErrorMessage) throws Exception {
        when(exchangeRateProvider.getConversionHistory(anyString(), anyString(), anyDouble(), any()))
                .thenThrow(new IllegalArgumentException(expectedErrorMessage));

        mockMvc.perform(get("/api/conversion-history")
                        .param("fromCurrency", fromCurrency)
                        .param("toCurrency", toCurrency)
                        .param("amount", amount)
                        .param("transactionDate", transactionDate.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(expectedErrorMessage));

        verify(exchangeRateProvider).getConversionHistory(anyString(), anyString(), anyDouble(), any());
    }

    private static Stream<Arguments> provideInvalidCurrencyConversionParams() {
        return Stream.of(Arguments.of("USD", "EUR", "-100", "Invalid amount"),
                Arguments.of("USD", "invalid", "100", "Invalid currency"));
    }

    private static Stream<Arguments> provideInvalidConversionHistoryParams() {
        LocalDate futureDate = LocalDate.now().plusDays(2);
        LocalDate sampleDate = LocalDate.now().minusDays(2);

        return Stream.of( arguments("USD", "EUR", "100.0", futureDate, "Invalid date"),
                arguments("USD", "invalid", "100.0", sampleDate, "Invalid currency"),
                arguments("USD", "EUR", "-100.0", sampleDate,"Invalid amount") );
    }
}