package com.team12.flightmanagement.cli.api;

import com.team12.flightmanagement.cli.model.Aircraft;
import com.team12.flightmanagement.cli.model.City;
import com.team12.flightmanagement.cli.model.Passenger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

class ApiClientTest {

    private ApiClient apiClient;

    @BeforeEach
    void setUp() {

        apiClient = spy(new ApiClient("http://example"));
    }

    @Test
    void testGetCities() throws IOException {
        String json = "[{\"id\":1,\"name\":\"St. John's\",\"province\":\"NL\",\"population\":110000," +
                "\"airports\":[{\"id\":7,\"name\":\"St. John's International\",\"code\":\"YYT\"}]}]";
        doReturn(json).when(apiClient).get("/cities");

        List<City> cities = apiClient.getCities();
        assertEquals(1, cities.size());
        assertEquals("St. John's", cities.get(0).getName());
        assertEquals("NL", cities.get(0).getProvince());
        assertEquals(1, cities.get(0).getAirports().size());
        assertEquals("YYT", cities.get(0).getAirports().get(0).getCode());
    }

    @Test
    void testGetPassengers() throws IOException {
        String json = "[{\"id\":1,\"firstName\":\"Jane\",\"lastName\":\"Doe\",\"phoneNumber\":\"709-123-1000\",\"aircraftList\":[]}]";
        doReturn(json).when(apiClient).get("/passengers");

        List<Passenger> out = apiClient.getPassengers();
        assertEquals(1, out.size());
        assertEquals("Jane", out.get(0).getFirstName());
        assertEquals("Doe", out.get(0).getLastName());
    }

    @Test
    void testGetAircraft() throws IOException {
        String json = "[{\"id\":5,\"type\":\"A220\",\"airlineName\":\"Air Canada\",\"numberOfPassengers\":2," +
                "\"passengers\":[],\"airports\":[{\"id\":7,\"name\":\"St. John's International\",\"code\":\"YYT\"}]}]";
        doReturn(json).when(apiClient).get("/aircraft");

        List<Aircraft> out = apiClient.getAircraft();
        assertEquals(1, out.size());
        assertEquals(5, out.get(0).getId());
        assertEquals("A220", out.get(0).getType());
        assertEquals("Air Canada", out.get(0).getAirlineName());
    }

    @Test
    void testGetPassengerById() throws IOException {
        String json = "{\"id\":2,\"firstName\":\"John\",\"lastName\":\"Smith\",\"phoneNumber\":\"709-123-2212\",\"aircraftList\":[]}";
        doReturn(json).when(apiClient).get("/passengers/2");

        Passenger p = apiClient.getPassengerById(2L);
        assertNotNull(p);
        assertEquals(2, p.getId());
        assertEquals("John", p.getFirstName());
        assertEquals("Smith", p.getLastName());
    }

    @Test
    void testGetAircraftById() throws IOException {
        String json = "{\"id\":10,\"type\":\"Airbus A320\",\"airlineName\":\"Air Canada\",\"numberOfPassengers\":2," +
                "\"passengers\":[],\"airports\":[]}";
        doReturn(json).when(apiClient).get("/aircraft/10");

        Aircraft aircraft = apiClient.getAircraftById(10L);
        assertEquals(10, aircraft.getId());
        assertEquals("Airbus A320", aircraft.getType());
        assertEquals("Air Canada", aircraft.getAirlineName());
        assertEquals(2, aircraft.getNumberOfPassengers());
    }

    // ===== New aggregated endpoint tests =====

    @Test
    void testGetAircraftWithPassengers() throws IOException {
        String json = "[" +
                "  {\"id\":1,\"type\":\"A220\",\"airlineName\":\"Air Canada\",\"passengers\":[\"Jane Doe\",\"John Smith\"]}," +
                "  {\"id\":2,\"type\":\"737 MAX\",\"airlineName\":\"WestJet\",\"passengers\":[\"Jane Doe\"]}" +
                "]";
        doReturn(json).when(apiClient).get("/aircraft-with-passengers");

        List<Map<String, Object>> rows = apiClient.getAircraftWithPassengers();
        assertEquals(2, rows.size());
        assertEquals("A220", rows.get(0).get("type"));
        assertEquals("Air Canada", rows.get(0).get("airlineName"));
        @SuppressWarnings("unchecked")
        List<String> pax = (List<String>) rows.get(0).get("passengers");
        assertTrue(pax.contains("Jane Doe"));
        assertTrue(pax.contains("John Smith"));
    }

    @Test
    void testGetAircraftWithAirports() throws IOException {
        String json = "[" +
                "  {\"id\":1,\"type\":\"A220\",\"airlineName\":\"Air Canada\",\"airports\":[\"YYT\",\"YHZ\"]}," +
                "  {\"id\":2,\"type\":\"737 MAX\",\"airlineName\":\"WestJet\",\"airports\":[\"YYT\"]}" +
                "]";
        doReturn(json).when(apiClient).get("/aircraft-with-airports");

        List<Map<String, Object>> rows = apiClient.getAircraftWithAirports();
        assertEquals(2, rows.size());
        assertEquals("A220", rows.get(0).get("type"));
        @SuppressWarnings("unchecked")
        List<String> codes = (List<String>) rows.get(0).get("airports");
        assertTrue(codes.contains("YYT"));
        assertTrue(codes.contains("YHZ"));
    }

    @Test
    void testGetPassengersWithAirports() throws IOException {
        String json = "[" +
                "  {\"passenger\":\"Jane Doe\",\"airports\":[\"St. John's International\",\"Halifax Stanfield\"]}," +
                "  {\"passenger\":\"John Smith\",\"airports\":[\"St. John's International\"]}" +
                "]";
        doReturn(json).when(apiClient).get("/passengers-with-airports");

        List<Map<String, Object>> rows = apiClient.getPassengersWithAirports();
        assertEquals(2, rows.size());
        assertEquals("Jane Doe", rows.get(0).get("passenger"));
        @SuppressWarnings("unchecked")
        List<String> names = (List<String>) rows.get(0).get("airports");
        assertTrue(names.contains("St. John's International"));
        assertTrue(names.contains("Halifax Stanfield"));
    }
}


