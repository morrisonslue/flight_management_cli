package com.team12.flightmanagement.cli.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Passenger {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getPhoneNumber() { return phoneNumber; }

    public void setId(Long id) { this.id = id; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}


