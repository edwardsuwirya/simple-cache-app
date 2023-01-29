package com.enigmacamp.cacheapp.service;

import com.enigmacamp.cacheapp.model.Customer;

public interface CustomerService {
    Customer registerCustomer(Customer newCustomer);

    Customer findCustomerById(String id) throws Exception;

    Customer updateProfile(Customer newProfile) throws Exception;

    void unregisterCustomer(String id) throws Exception;
}
