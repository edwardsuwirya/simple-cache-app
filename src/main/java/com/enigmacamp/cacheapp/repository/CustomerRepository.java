package com.enigmacamp.cacheapp.repository;

import com.enigmacamp.cacheapp.model.Customer;

import java.util.Optional;

public interface CustomerRepository {
    Customer save(Customer newCustomer);

    Optional<Customer> findById(String id);

    void delete(Customer customer);
}
