package com.team12.flightmanagement.cli.app;

import com.team12.flightmanagement.cli.api.ApiClient;
import com.team12.flightmanagement.cli.model.City;
import com.team12.flightmanagement.cli.model.Aircraft;
import com.team12.flightmanagement.cli.model.Airport;
import com.team12.flightmanagement.cli.model.Passenger;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        String apiBase = "http://localhost:8080";
        ApiClient apiClient = new ApiClient(apiBase);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nFlight Management CLI");
            System.out.println("1. What airports are there in each city?");
            System.out.println("2. What aircraft has each passenger flown on?");
            System.out.println("3. What airports do aircraft take off from and land at?");
            System.out.println("4. What airports have passengers used?");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");
            String choice = scanner.nextLine();
            try {
                switch (choice) {
                    case "1":
                        // Airports in each city
                        List<City> cities = apiClient.getCities();
                        if (cities.isEmpty()) {
                            System.out.println("No cities found.");
                        } else {
                            for (City city : cities) {
                                System.out.println(city.getName() + ", " + city.getProvince() + ":");
                                if (city.getAirports() != null && !city.getAirports().isEmpty()) {
                                    for (Airport airport : city.getAirports()) {
                                        System.out.println("  - " + airport.getName() + " (" + airport.getCode() + ")");
                                    }
                                } else {
                                    System.out.println("  (No airports)");
                                }
                            }
                        }
                        break;
                    case "2":
                        // Aircraft per passenger
                        List<Passenger> passengers = apiClient.getPassengers();
                        List<Aircraft> aircraftList = apiClient.getAircraft();
                        if (passengers.isEmpty()) {
                            System.out.println("No passengers found.");
                        } else {
                            for (Passenger p : passengers) {
                                System.out.print(p.getFirstName() + " " + p.getLastName() + " has flown on: ");
                                List<String> flownAircraft = new ArrayList<>();
                                for (Aircraft a : aircraftList) {
                                    if (a.getPassengers() != null) {
                                        for (Passenger pax : a.getPassengers()) {
                                            if (Objects.equals(pax.getId(), p.getId())) {
                                                flownAircraft.add(a.getType() + " (" + a.getAirlineName() + ")");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (flownAircraft.isEmpty()) {
                                    System.out.println("No aircraft");
                                } else {
                                    System.out.println(String.join(", ", flownAircraft));
                                }
                            }
                        }
                        break;
                    case "3":
                        // Airports per aircraft
                        List<Aircraft> aircrafts = apiClient.getAircraft();
                        if (aircrafts.isEmpty()) {
                            System.out.println("No aircraft found.");
                        } else {
                            for (Aircraft ac : aircrafts) {
                                System.out.print(ac.getType() + " (" + ac.getAirlineName() + ") uses airports: ");
                                if (ac.getAirports() != null && !ac.getAirports().isEmpty()) {
                                    List<String> airportNames = new ArrayList<>();
                                    for (Airport ap : ac.getAirports()) {
                                        airportNames.add(ap.getName() + " (" + ap.getCode() + ")");
                                    }
                                    System.out.println(String.join(", ", airportNames));
                                } else {
                                    System.out.println("No airports");
                                }
                            }
                        }
                        break;
                    case "4":
                        // Airports used by each passenger (aggregate via aircraft)
                        passengers = apiClient.getPassengers();
                        aircraftList = apiClient.getAircraft();
                        if (passengers.isEmpty()) {
                            System.out.println("No passengers found.");
                        } else {
                            for (Passenger p : passengers) {
                                Set<String> airportNames = new HashSet<>();
                                for (Aircraft a : aircraftList) {
                                    boolean passengerOnAircraft = false;
                                    if (a.getPassengers() != null) {
                                        for (Passenger pax : a.getPassengers()) {
                                            if (Objects.equals(pax.getId(), p.getId())) {
                                                passengerOnAircraft = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (passengerOnAircraft && a.getAirports() != null) {
                                        for (Airport ap : a.getAirports()) {
                                            airportNames.add(ap.getName() + " (" + ap.getCode() + ")");
                                        }
                                    }
                                }
                                System.out.print(p.getFirstName() + " " + p.getLastName() + " has used airports: ");
                                if (airportNames.isEmpty()) {
                                    System.out.println("No airports");
                                } else {
                                    System.out.println(String.join(", ", airportNames));
                                }
                            }
                        }
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}



