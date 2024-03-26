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

@Service
public class CurrencyService {

    @Value("${exchange_key}")
    private String key;
    public static final String BASE_URL = "http://api.currencylayer.com/";
    public static final String EXCHANGE_RATE_ENDPOINT = "live";
    static CloseableHttpClient httpClient = HttpClients.createDefault();

    public String getExchangeRate(String baseCurrency, String targetCurrency) {
        baseCurrency = baseCurrency.toUpperCase();
        targetCurrency = targetCurrency.toUpperCase();

        StringBuilder stringBuilder = new StringBuilder();
        HttpGet get = new HttpGet(BASE_URL + EXCHANGE_RATE_ENDPOINT + "?access_key=" + key + "&source=" + baseCurrency + "&currencies=" + targetCurrency);

        try {
            CloseableHttpResponse response =  httpClient.execute(get);
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
}