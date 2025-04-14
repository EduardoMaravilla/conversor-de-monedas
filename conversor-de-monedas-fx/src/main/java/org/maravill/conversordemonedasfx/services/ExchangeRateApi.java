package org.maravill.conversordemonedasfx.services;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ExchangeRageApi {

    public static <T> T obtenerInfo(String url, Class<T> clazz) {
        URI uriExchange = URI.create(url);
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest
                .newBuilder().uri(uriExchange).GET().build();
            HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());
            Gson gson = new Gson();
            return gson.fromJson(response.body(), clazz);
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
