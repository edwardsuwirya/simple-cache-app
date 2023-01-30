package com.enigmacamp.cacheapp.repository;

import com.enigmacamp.cacheapp.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, String> {
}
