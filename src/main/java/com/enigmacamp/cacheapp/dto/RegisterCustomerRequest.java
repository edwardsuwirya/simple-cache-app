package com.enigmacamp.cacheapp.dto;


public class RegisterCustomerRequest {
    private String fullName;
    private String address;

    public RegisterCustomerRequest(String fullName, String address) {
        this.fullName = fullName;
        this.address = address;
    }

    public RegisterCustomerRequest() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
