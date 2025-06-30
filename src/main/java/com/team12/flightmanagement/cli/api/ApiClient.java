package com.team12.flightmanagement.cli.api;

import com.team12.flightmanagement.cli.model.City;
import com.team12.flightmanagement.cli.model.Passenger;
import com.team12.flightmanagement.cli.model.Aircraft;
import com.team12.flightmanagement.cli.util.JsonUtil;

import java.io.IOException;
import java.util.*;

public class ApiClient {
    private final String apiUrl;

    public ApiClient(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    protected String get(String path) throws IOException {
        java.net.URL url = new java.net.URL(apiUrl + path);
        java.net.HttpURLConnection con = (java.net.HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Accept", "application/json");
        java.io.InputStream is = con.getInputStream();
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        String result = s.hasNext() ? s.next() : "";
        is.close();
        return result;
    }

    public List<City> getCities() throws IOException {
        String json = get("/cities");
        if (json == null || json.isBlank() || json.equals("null")) return Collections.emptyList();
        City[] cities = JsonUtil.fromJsonArray(json, City[].class);
        return Arrays.asList(cities);
    }

    public List<Passenger> getPassengers() throws IOException {
        String json = get("/passengers");
        if (json == null || json.isBlank() || json.equals("null")) return Collections.emptyList();
        Passenger[] passengers = JsonUtil.fromJsonArray(json, Passenger[].class);
        return Arrays.asList(passengers);
    }

    public List<Aircraft> getAircraft() throws IOException {
        String json = get("/aircraft");
        if (json == null || json.isBlank() || json.equals("null")) return Collections.emptyList();
        Aircraft[] aircraft = JsonUtil.fromJsonArray(json, Aircraft[].class);
        return Arrays.asList(aircraft);
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




