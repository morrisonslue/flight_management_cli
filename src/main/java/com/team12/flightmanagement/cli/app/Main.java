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
        if (args != null && args.length > 0 && args[0] != null && !args[0].isBlank()) {
            apiBase = args[0].trim();
        } else {
            String env = System.getenv("BASE_URL");
            if (env != null && !env.isBlank()) {
                apiBase = env.trim();
            }
        }

        ApiClient apiClient = new ApiClient(apiBase);
        Scanner scanner = new Scanner(System.in);

        // test mode
        if ("true".equalsIgnoreCase(System.getProperty("cli.test"))) {
            System.out.println("(test mode) starting then exiting without input");
            return;
        }

        while (true) {
            System.out.println("\nFlight Management CLI");
            System.out.println("1. What airports are there in each city");
            System.out.println("2. What aircraft has each passenger flown on");
            System.out.println("3. What airports do aircraft take off from and land at");
            System.out.println("4. What airports have passengers used");
            System.out.println("0. Exit");
            System.out.print("pick an option: ");
            String choice = scanner.nextLine();

            try {
                switch (choice) {
                    case "1": {
                        // cities and their airports
                        List<City> cities = apiClient.getCities();
                        if (cities.isEmpty()) {
                            System.out.println("no cities found");
                        } else {
                            for (City city : cities) {
                                System.out.println(city.getName() + ", " + city.getProvince() + ":");
                                if (city.getAirports() != null && !city.getAirports().isEmpty()) {
                                    for (Airport airport : city.getAirports()) {
                                        System.out.println("  - " + airport.getName() + " (" + airport.getCode() + ")");
                                    }
                                } else {
                                    System.out.println("  (no airports)");
                                }
                            }
                        }
                        break;
                    }

                    case "2": {
                        // aircraft each passenger has flown on
                        List<Passenger> passengers = apiClient.getPassengers();
                        List<Aircraft> aircraftList = apiClient.getAircraft();
                        if (passengers.isEmpty()) {
                            System.out.println("no passengers found");
                        } else {
                            for (Passenger p : passengers) {
                                System.out.print(p.getFirstName() + " " + p.getLastName() + " has flown on ");
                                List<String> flownAircraft = new ArrayList<>();
                                for (Aircraft a : aircraftList) {
                                    if (a.getPassengers() != null) {
                                        for (Passenger pax : a.getPassengers()) {
                                            if (pax.getId() == p.getId()) {
                                                flownAircraft.add(a.getType() + " (" + a.getAirlineName() + ")");
                                                break;
                                            }
                                        }
                                    }
                                }
                                if (flownAircraft.isEmpty()) {
                                    System.out.println("no aircraft");
                                } else {
                                    System.out.println(String.join(", ", flownAircraft));
                                }
                            }
                        }
                        break;
                    }

                    case "3": {
                        // airports used by each aircraft
                        List<Aircraft> aircraftList = apiClient.getAircraft();
                        if (aircraftList.isEmpty()) {
                            System.out.println("no aircraft found");
                        } else {
                            for (Aircraft ac : aircraftList) {
                                System.out.print(ac.getType() + " (" + ac.getAirlineName() + ") uses airports ");
                                if (ac.getAirports() != null && !ac.getAirports().isEmpty()) {
                                    List<String> airportNames = new ArrayList<>();
                                    for (Airport ap : ac.getAirports()) {
                                        airportNames.add(ap.getName() + " (" + ap.getCode() + ")");
                                    }
                                    System.out.println(String.join(", ", airportNames));
                                } else {
                                    System.out.println("no airports");
                                }
                            }
                        }
                        break;
                    }

                    case "4": {
                        // airports that each passenger has used
                        List<Passenger> passengers = apiClient.getPassengers();
                        List<Aircraft> aircraftList = apiClient.getAircraft();
                        if (passengers.isEmpty()) {
                            System.out.println("no passengers found");
                        } else {
                            for (Passenger p : passengers) {
                                Set<String> airportNames = new HashSet<>();
                                for (Aircraft a : aircraftList) {
                                    boolean match = false;
                                    if (a.getPassengers() != null) {
                                        for (Passenger pax : a.getPassengers()) {
                                            if (pax.getId() == p.getId()) {
                                                match = true;
                                                break;
                                            }
                                        }
                                    }
                                    if (match && a.getAirports() != null) {
                                        for (Airport ap : a.getAirports()) {
                                            airportNames.add(ap.getName() + " (" + ap.getCode() + ")");
                                        }
                                    }
                                }
                                System.out.print(p.getFirstName() + " " + p.getLastName() + " has used airports ");
                                if (airportNames.isEmpty()) {
                                    System.out.println("no airports");
                                } else {
                                    System.out.println(String.join(", ", airportNames));
                                }
                            }
                        }
                        break;
                    }

                    case "0":
                        System.out.println("Exiting");
                        return;

                    default:
                        System.out.println("selection not valid");
                }

            } catch (Exception e) {
                System.out.println("error - " + e.getMessage());
            }
        }
    }
}






