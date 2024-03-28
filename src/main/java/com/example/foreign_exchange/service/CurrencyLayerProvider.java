package com.example.foreign_exchange.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class CurrencyLayerProvider implements ExchangeRateProvider {
    @Value("${exchange_key}")
    private String key;
    private static final String BASE_URL = "http://api.currencylayer.com/";
    private static final String EXCHANGE_RATE_ENDPOINT = "live";
    private static final String CURRENCY_CONVERSION_ENDPOINT = "convert";
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    @Autowired
    private ExchangeRateValidationService validationService;

    @Override
    public String getExchangeRate(String baseCurrency, String targetCurrency) {
        baseCurrency = baseCurrency.toUpperCase();
        targetCurrency = targetCurrency.toUpperCase();
        validationService.validateCurrencyCodes(baseCurrency, targetCurrency);

        StringBuilder stringBuilder = new StringBuilder();
        HttpGet get = new HttpGet(BASE_URL + EXCHANGE_RATE_ENDPOINT + "?access_key=" + key + "&source=" + baseCurrency + "&currencies=" + targetCurrency);

        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(get);
            HttpEntity entity = response.getEntity();

            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));
            boolean success = exchangeRates.getBoolean("success");

            if (!success) {
                throw new IllegalArgumentException("You have supplied an invalid Source Currency. [Example: source = USD]");
            }

            stringBuilder.append("Currency Exchange Rate from ").append(baseCurrency)
                    .append(" to ").append(targetCurrency).append("\n");

            double rate = exchangeRates.getJSONObject("quotes").getDouble(baseCurrency + targetCurrency);

            stringBuilder.append("1 ").append(baseCurrency).append(" = ").append(rate)
                    .append(" ").append(targetCurrency).append("\n");

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        return stringBuilder.toString();
    }

    @Override
    public String getCurrencyConversion(String fromCurrency, String toCurrency, BigDecimal amount) {
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        validationService.validateCurrencyCodes(fromCurrency, toCurrency);
        validationService.validateAmount(amount);

        StringBuilder stringBuilder = new StringBuilder();

        HttpGet get = new HttpGet(BASE_URL + CURRENCY_CONVERSION_ENDPOINT + "?access_key=" + key + "&from=" + fromCurrency + "&to=" + toCurrency + "&amount=" + amount);

        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(get);
            HttpEntity entity = response.getEntity();
            JSONObject conversionResult = new JSONObject(EntityUtils.toString(entity));
            boolean success = conversionResult.getBoolean("success");

            if (!success) {
                throw new IllegalArgumentException("You have supplied an invalid Source Currency. [Example: source = USD]");
            }

            String transactionId = response.getFirstHeader("x-apilayer-transaction-id").getValue();

            BigDecimal convertedAmount = conversionResult.getBigDecimal("result");

            stringBuilder.append("Unique transaction identifier: ").append(transactionId)
                    .append("\nConverted amount: ").append(convertedAmount);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e.getMessage());
        }

        return stringBuilder.toString();
    }

    @Override
    public String getConversionHistory(String fromCurrency, String toCurrency, double amount, LocalDate transactionDate) {
        fromCurrency = fromCurrency.toUpperCase();
        toCurrency = toCurrency.toUpperCase();

        validationService.validateCurrencyCodes(fromCurrency, toCurrency);
        validationService.validateAmount(amount);

        StringBuilder stringBuilder = new StringBuilder();

        String formattedDate = transactionDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        HttpGet get = new HttpGet(BASE_URL + CURRENCY_CONVERSION_ENDPOINT +
                "?access_key=" + key +
                "&from=" + fromCurrency +
                "&to=" + toCurrency +
                "&amount=" + amount +
                "&date=" + formattedDate);

        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(get);
            HttpEntity entity = response.getEntity();
            JSONObject conversionData = new JSONObject(EntityUtils.toString(entity));
            boolean success = conversionData.getBoolean("success");

            if (!success) {
                throw new IllegalArgumentException("You have supplied an invalid Source Currency. [Example: source = USD]");
            }

            boolean historical = conversionData.getBoolean("historical");
            if (historical) {
                JSONObject query = conversionData.getJSONObject("query");
                double result = conversionData.getDouble("result");
                String date = conversionData.getString("date");

                stringBuilder.append("Conversion from ")
                        .append(query.getString("from"))
                        .append(" to ")
                        .append(query.getString("to"))
                        .append(" on ")
                        .append(date)
                        .append("\n")
                        .append(amount)
                        .append(" ")
                        .append(query.getString("from"))
                        .append(" = ")
                        .append(result)
                        .append(" ")
                        .append(query.getString("to"));
            } else {
                throw new IllegalArgumentException("There is no record for this date.");
            }
        } catch (IOException | JSONException e) {
            throw new IllegalArgumentException("Invalid date");
        }

        return stringBuilder.toString();
    }
}