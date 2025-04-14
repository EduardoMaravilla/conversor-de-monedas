package org.maravill.conversordemonedasfx.services;

import org.json.JSONArray;
import org.json.JSONObject;
import org.maravill.conversordemonedasfx.models.ConversionRate;
import org.maravill.conversordemonedasfx.models.Currency;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

public final class ExchangeRateApi {
    private static final String URL_API = System.getenv("ExchangeRateApi");
    private static final String SUPPORT_CODE_NAME = "supported_codes";
    private static final String CODES_PATH="/codes";
    private static final String LATEST_PATH="/latest/";
    private static final int OK_VALUE = 200;

    private ExchangeRateApi() {
    }

    public static List<Currency> getSupportCodesFromApi() {
        Optional<String> codes = getInfo(URL_API + CODES_PATH);
        if (codes.isEmpty()) {
            return Collections.emptyList();
        }
        JSONObject jsonObject = new JSONObject(codes.get());
        JSONArray supportedCodes = jsonObject.getJSONArray(SUPPORT_CODE_NAME);
        List<Currency> currencyCodes = new ArrayList<>();
        for (int i = 0; i < supportedCodes.length(); i++) {
            JSONArray code = supportedCodes.getJSONArray(i);
            currencyCodes.add(new Currency(
                code.getString(0),
                code.getString(1)
            ));
        }
        return currencyCodes;
    }

    private static Optional<String> getInfo(String url) {
        URI uriExchange = URI.create(url);
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                .uri(uriExchange).GET().build();
            HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != OK_VALUE) {
                return Optional.empty();
            }
            return Optional.of(response.body());
        } catch (IOException | InterruptedException e) {
            return Optional.empty();
        }
    }

    public static ConversionRate getConversionRateFromBaseCode(String baseCode) {
        Optional<String> rates = getInfo(URL_API + LATEST_PATH + baseCode);
        if (rates.isEmpty()) {
            return null;
        }
        JSONObject jsonObject = new JSONObject(rates.get());
        String baseCodeRate = jsonObject.getString("base_code");
        JSONObject ratesJson = jsonObject.getJSONObject("conversion_rates");
        Map<String, Double> conversionRates = new HashMap<>();
        for (String key : ratesJson.keySet()) {
            conversionRates.put(key, ratesJson.getDouble(key));
        }
        return new ConversionRate(baseCodeRate,conversionRates);
    }
}