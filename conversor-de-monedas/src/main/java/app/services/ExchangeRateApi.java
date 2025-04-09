package app.services;

import app.models.ExchangeResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRateApi {
    private static final String URL_API = "https://v6.exchangerate-api.com/v6/ad69bf59500dfcdba92cc3b5/pair";

    public static Double obtenerConversion(String firstCoin, String secondCoin, double amount)
        throws IOException, InterruptedException {
        URI uriConversion=URI.create(URL_API+"/"+firstCoin+"/"+secondCoin+"/"+amount);
        HttpClient client= HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder().uri(uriConversion).GET().build();

        HttpResponse<String> response = client.send(request,HttpResponse.BodyHandlers.ofString());
        Gson gson = new Gson();
        ExchangeResponse exchangeResponse = gson.fromJson(response.body(), ExchangeResponse.class);
        client.close();
        return exchangeResponse.conversion_result();
    }
}
