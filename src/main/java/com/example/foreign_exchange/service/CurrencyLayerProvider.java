package com.example.foreign_exchange.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;

@Service
public class CurrencyLayerProvider implements ExchangeRateProvider {
    @Value("${exchange_key}")
    private String key;
    private static final String BASE_URL = "http://api.currencylayer.com/";
    private static final String EXCHANGE_RATE_ENDPOINT = "live";
    private static final String CURRENCY_CONVERSION_ENDPOINT = "convert";
    private static final CloseableHttpClient HTTP_CLIENT = HttpClients.createDefault();

    @Override
    public String getExchangeRate(String baseCurrency, String targetCurrency) {
        baseCurrency = baseCurrency.toUpperCase();
        targetCurrency = targetCurrency.toUpperCase();

        StringBuilder stringBuilder = new StringBuilder();
        HttpGet get = new HttpGet(BASE_URL + EXCHANGE_RATE_ENDPOINT + "?access_key=" + key + "&source=" + baseCurrency + "&currencies=" + targetCurrency);

        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(get);
            HttpEntity entity = response.getEntity();

            JSONObject exchangeRates = new JSONObject(EntityUtils.toString(entity));

            stringBuilder.append("Currency Exchange Rate from ").append(baseCurrency).append(" to ").append(targetCurrency).append("\n");

            double rate = exchangeRates.getJSONObject("quotes").getDouble(baseCurrency + targetCurrency);
            stringBuilder.append("1 ").append(baseCurrency).append(" = ").append(rate).append(" ").append(targetCurrency).append("\n");

            response.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            stringBuilder.append("Error fetching exchange rates");
        }

        return stringBuilder.toString();
    }

    @Override
    public String getCurrencyConversion(String from, String to, BigDecimal amount) {
        from = from.toUpperCase();
        to = to.toUpperCase();

        StringBuilder stringBuilder = new StringBuilder();

        HttpGet get = new HttpGet(BASE_URL + CURRENCY_CONVERSION_ENDPOINT + "?access_key=" + key + "&from=" + from + "&to=" + to + "&amount=" + amount);

        BigDecimal convertedAmount = BigDecimal.ZERO;
        try {
            CloseableHttpResponse response = HTTP_CLIENT.execute(get);
            String transactionId = response.getFirstHeader("x-apilayer-transaction-id").getValue();

            HttpEntity entity = response.getEntity();

            JSONObject conversionResult = new JSONObject(EntityUtils.toString(entity));

            convertedAmount = conversionResult.getBigDecimal("result");

            stringBuilder.append("Unique transaction identifier: ").append(transactionId)
                    .append("\n").append("Converted amount: ").append(convertedAmount);

            response.close();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            stringBuilder.append("Error converting currency");
        }

        return stringBuilder.toString();
    }
}