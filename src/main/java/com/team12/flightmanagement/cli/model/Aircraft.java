package com.team12.flightmanagement.cli.model;

import java.util.List;

public class Aircraft {
    public Long id;
    public String type;
    public String airlineName;
    public int numberOfPassengers;
    public List<Passenger> passengers;
    public List<Airport> airports;
}

