package com.team12.flightmanagement.cli.app;

import com.team12.flightmanagement.cli.api.ApiClient;
import com.team12.flightmanagement.cli.model.City;
import com.team12.flightmanagement.cli.model.Aircraft;
import com.team12.flightmanagement.cli.model.Airport;
import com.team12.flightmanagement.cli.model.Passenger;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        // NOTE TO SELF!!! base url can come from command line or env or default
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

                        List<Map<String, Object>> acWithPassengers = apiClient.getAircraftWithPassengers();

                        // build passenger -> list of aircraft strings
                        Map<String, List<String>> byPassenger = new LinkedHashMap<>();
                        for (Map<String, Object> row : acWithPassengers) {
                            String type = Objects.toString(row.get("type"), "");
                            String airline = Objects.toString(row.get("airlineName"), "");
                            String aircraftLabel = type + (airline.isBlank() ? "" : " (" + airline + ")");

                            @SuppressWarnings("unchecked")
                            List<String> names = (List<String>) row.get("passengers");
                            if (names != null) {
                                for (String name : names) {
                                    if (name == null || name.isBlank()) continue;
                                    byPassenger.computeIfAbsent(name, k -> new ArrayList<>()).add(aircraftLabel);
                                }
                            }
                        }

                        if (byPassenger.isEmpty()) {
                            System.out.println("no data");
                        } else {
                            for (Map.Entry<String, List<String>> e : byPassenger.entrySet()) {
                                String name = e.getKey();
                                List<String> aircraftList = e.getValue();
                                System.out.print(name + " has flown on ");
                                if (aircraftList == null || aircraftList.isEmpty()) {
                                    System.out.println("no aircraft");
                                } else {
                                    System.out.println(String.join(", ", aircraftList));
                                }
                            }
                        }
                        break;
                    }

                    case "3": {
                        // airports used by each plane for flights

                        List<Map<String, Object>> acWithAirports = apiClient.getAircraftWithAirports();
                        if (acWithAirports.isEmpty()) {
                            System.out.println("no aircraft found");
                        } else {
                            for (Map<String, Object> row : acWithAirports) {
                                String type = Objects.toString(row.get("type"), "");
                                String airline = Objects.toString(row.get("airlineName"), "");
                                @SuppressWarnings("unchecked")
                                List<String> codes = (List<String>) row.get("airports");

                                System.out.print(type + (airline.isBlank() ? "" : " (" + airline + ")") + " uses airports ");
                                if (codes == null || codes.isEmpty()) {
                                    System.out.println("no airports");
                                } else {
                                    System.out.println(String.join(", ", codes));
                                }
                            }
                        }
                        break;
                    }

                    case "4": {
                        // airports that each passenger has used (GET /passengers-with-airports)

                        List<Map<String, Object>> paxWithAirports = apiClient.getPassengersWithAirports();
                        if (paxWithAirports.isEmpty()) {
                            System.out.println("no passengers found");
                        } else {
                            for (Map<String, Object> row : paxWithAirports) {
                                String fullName = Objects.toString(row.get("passenger"), "");
                                @SuppressWarnings("unchecked")
                                List<String> names = (List<String>) row.get("airports");

                                System.out.print(fullName + " has used airports ");
                                if (names == null || names.isEmpty()) {
                                    System.out.println("no airports");
                                } else {
                                    System.out.println(String.join(", ", names));
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







