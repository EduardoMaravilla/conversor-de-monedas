package app.services;

import app.models.ExchangeResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public final class ExchangeRateApi {
    private static final String URL_API = "https://v6.exchangerate-api.com/v6/";
    private static final String API_KEY = "eb33712be98e6d9c032614a3"; //write your API key here
    private static final String PAIR_PATH = "/pair";

    private ExchangeRateApi() {}

    public static Double obtenerConversion(String firstCoin, String secondCoin, double amount)
        throws IOException, InterruptedException {
        URI uriConversion = URI.create(URL_API + API_KEY + PAIR_PATH +
            "/" + firstCoin + "/" + secondCoin + "/" + amount);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uriConversion).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        ExchangeResponse exchangeResponse = gson.fromJson(response.body(), ExchangeResponse.class);
        client.close();
        return exchangeResponse.conversion_result();
    }
}
