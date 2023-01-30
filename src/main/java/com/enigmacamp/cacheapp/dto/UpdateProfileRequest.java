package com.enigmacamp.cacheapp.dto;


public class UpdateProfileRequest {
    private String customerId;
    private String address;

    public UpdateProfileRequest(String customerId, String address) {
        this.customerId = customerId;
        this.address = address;
    }

    public UpdateProfileRequest() {
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
