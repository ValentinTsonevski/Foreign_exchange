package com.example.foreign_exchange.controller;

import com.example.foreign_exchange.service.ExchangeRateProvider;
import io.swagger.v3.oas.annotations.Operation;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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

    @Operation(
            summary = "Get exchange rate",
            description = "Get exchange rate between 2 currencies",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Currency Exchange Rate from USD to BGN\n1 USD = 1.806733 BGN"))
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "text/plain",
                            examples = @ExampleObject(name = "Invalid currency",
                                    value = "You have supplied an invalid Source Currency. [Example: source = USD]"))
            )
            }
    )
    @GetMapping("/exchange-rates")
    public ResponseEntity<String> getExchangeRates(@RequestParam("baseCurrency") String baseCurrency,
                                                   @RequestParam("targetCurrency") String targetCurrency) {
        try {
            String exchangeRateResponse = exchangeRateProvider.getExchangeRate(baseCurrency, targetCurrency);
            return ResponseEntity.ok(exchangeRateResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Get conversion rate",
            description = "Get conversion rate between 2 currencies for given amount",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "Unique transaction identifier: 2ade2bc6-4b42-47a9-8093-912c2d79ac1a\nConverted amount: 180.7413"))
                    ),@ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "text/plain", examples = {
                            @ExampleObject(name = "Invalid currency",
                                    value = "You have supplied an invalid Source Currency. [Example: source = USD]"),
                            @ExampleObject(name = "Invalid amount",
                                    value = "You have supplied an invalid amount.The amount must be positive [Example: amount = 100]")
                    })
            )
            }
    )
    @GetMapping("/conversion")
    public ResponseEntity<String> getCurrencyConversion(@RequestParam("fromCurrency") String fromCurrency,
                                        @RequestParam("toCurrency") String toCurrency,
                                        @RequestParam("amount") BigDecimal amount) {
        try {
            String currencyConversion = exchangeRateProvider.getCurrencyConversion(fromCurrency, toCurrency, amount);
            return ResponseEntity.ok(currencyConversion);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @Operation(
            summary = "Get conversion rate at past date",
            description = "Get conversion rate between 2 currencies for given amount at past date",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(mediaType = "text/plain",
                                    examples = @ExampleObject(value = "Conversion from USD to BGN on 2014-02-10\n" + "100.0 USD = 143.5348 BGN"))
                    ), @ApiResponse(
                    responseCode = "400",
                    description = "Bad request",
                    content = @Content(mediaType = "text/plain", examples = {
                            @ExampleObject(name = "Invalid currency",
                                    value = "You have supplied an invalid Source Currency. [Example: source = USD]"),
                            @ExampleObject(name = "Invalid amount",
                                    value = "You have supplied an invalid amount.The amount must be positive [Example: amount = 100]"),
                            @ExampleObject(name = "Invalid date",
                                    value = "Invalid date"),
                    }
            ))

            }
    )
    @GetMapping("/conversion-history")
    public ResponseEntity<String> getConversionHistory(
            @RequestParam("fromCurrency") String fromCurrency,
            @RequestParam("toCurrency") String toCurrency,
            @RequestParam("amount") double amount,
            @RequestParam("transactionDate") LocalDate transactionDate
    ) {
        try {
            String conversionHistory = exchangeRateProvider.getConversionHistory(fromCurrency, toCurrency, amount, transactionDate);
            return ResponseEntity.ok(conversionHistory);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
