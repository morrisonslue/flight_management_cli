package com.team12.flightmanagement.cli.api;

import com.team12.flightmanagement.cli.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ApiClientTest {

    private ApiClient apiClient;

    @BeforeEach
    void setUp() {
        apiClient = Mockito.spy(new ApiClient("http://localhost:8080"));
    }

    @Test
    void testGetCities() throws IOException {
        String json = "[" +
                "{\"id\":1,\"name\":\"St. John's\",\"province\":\"NL\",\"population\":110000,\"airports\":[]}" +
                "]";
        doReturn(json).when(apiClient).get("/cities");

        List<City> cities = apiClient.getCities();
        assertEquals(1, cities.size());
        assertEquals("St. John's", cities.get(0).getName());
        assertEquals("NL", cities.get(0).getProvince());
        assertEquals(110000, cities.get(0).getPopulation());
        assertNotNull(cities.get(0).getAirports());
    }

    @Test
    void testGetPassengers() throws IOException {
        String json = "[" +
                "{\"id\":42,\"firstName\":\"Chris\",\"lastName\":\"Morrison\",\"phoneNumber\":\"555-5555\"}" +
                "]";
        doReturn(json).when(apiClient).get("/passengers");

        List<Passenger> passengers = apiClient.getPassengers();
        assertEquals(1, passengers.size());
        assertEquals("Chris", passengers.get(0).getFirstName());
        assertEquals("Morrison", passengers.get(0).getLastName());
        assertEquals("555-5555", passengers.get(0).getPhoneNumber());
    }

    @Test
    void testGetAircraft() throws IOException {
        String json = "[" +
                "{\"id\":3,\"type\":\"Boeing 747\",\"airlineName\":\"TestAir\",\"numberOfPassengers\":0,\"passengers\":[],\"airports\":[]}" +
                "]";
        doReturn(json).when(apiClient).get("/aircraft");

        List<Aircraft> aircraftList = apiClient.getAircraft();
        assertEquals(1, aircraftList.size());
        assertEquals("Boeing 747", aircraftList.get(0).getType());
        assertEquals("TestAir", aircraftList.get(0).getAirlineName());
    }

    @Test
    void testGetPassengerById() throws IOException {
        String json = "{\"id\":5,\"firstName\":\"Rick\",\"lastName\":\"Sanchez\",\"phoneNumber\":\"555-8888\" }";
        doReturn(json).when(apiClient).get("/passengers/5");

        Passenger passenger = apiClient.getPassengerById(5L);
        assertEquals(5, passenger.getId());
        assertEquals("Rick", passenger.getFirstName());
        assertEquals("Sanchez", passenger.getLastName());
        assertEquals("555-8888", passenger.getPhoneNumber());
    }

    @Test
    void testGetAircraftById() throws IOException {
        String json = "{\"id\":10,\"type\":\"Airbus A320\",\"airlineName\":\"Air Canada\",\"numberOfPassengers\":2,\"passengers\":[],\"airports\":[]}";
        doReturn(json).when(apiClient).get("/aircraft/10");

        Aircraft aircraft = apiClient.getAircraftById(10L);
        assertEquals(10, aircraft.getId());
        assertEquals("Airbus A320", aircraft.getType());
        assertEquals("Air Canada", aircraft.getAirlineName());
        assertEquals(2, aircraft.getNumberOfPassengers());
    }
}


