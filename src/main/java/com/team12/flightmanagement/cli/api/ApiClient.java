package com.team12.flightmanagement.cli.api;

import com.team12.flightmanagement.cli.model.City;
import com.team12.flightmanagement.cli.model.Passenger;
import com.team12.flightmanagement.cli.model.Aircraft;
import com.team12.flightmanagement.cli.util.JsonUtil;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

public class ApiClient {
    private final String apiUrl;
    private final HttpClient http;

    public ApiClient(String apiUrl) {
        this.apiUrl = apiUrl;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
    }

    protected String get(String path) throws IOException {
        try {
            HttpRequest req = HttpRequest.newBuilder()
                    .uri(URI.create(apiUrl + path))
                    .timeout(Duration.ofSeconds(10))
                    .GET()
                    .build();

            HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());
            int code = res.statusCode();
            if (code >= 200 && code < 300) {
                return res.body();
            } else {
                throw new IOException("http " + code + " for " + path);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IOException("request interrupted", e);
        }
    }

    // /aircraft
    public List<Aircraft> getAircraft() throws IOException {
        String json = get("/aircraft");
        return JsonUtil.fromJsonList(json, Aircraft.class);
    }

    // /cities
    public List<City> getCities() throws IOException {
        String json = get("/cities");
        return JsonUtil.fromJsonList(json, City.class);
    }

    // passengers
    public List<Passenger> getPassengers() throws IOException {
        String json = get("/passengers");
        return JsonUtil.fromJsonList(json, Passenger.class);
    }

    public Passenger getPassengerById(long id) throws IOException {
        String json = get("/passengers/" + id);
        if (json == null || json.isBlank() || json.equals("null")) return null;
        return JsonUtil.fromJson(json, Passenger.class);
    }

    public Aircraft getAircraftById(long id) throws IOException {
        String json = get("/aircraft/" + id);
        if (json == null || json.isBlank() || json.equals("null")) return null;
        return JsonUtil.fromJson(json, Aircraft.class);
    }
}



