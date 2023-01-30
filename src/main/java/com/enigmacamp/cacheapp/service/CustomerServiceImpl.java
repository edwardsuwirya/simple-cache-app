package com.enigmacamp.cacheapp.service;

import com.enigmacamp.cacheapp.model.Customer;
import com.enigmacamp.cacheapp.repository.CustomerRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;

    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public Customer registerCustomer(Customer newCustomer) {
        return customerRepository.save(newCustomer);
    }

    @Cacheable(
            value = "customer-cache",
            key = "#id"
    )
    @Override
    public Customer findCustomerById(String id) throws Exception {
        return customerRepository.findById(id).orElseThrow(() -> new Exception("Customer not found"));
    }

    @CachePut(
            value = "customer-cache",
            key = "#newProfile.customerId"
    )
    @Override
    public Customer updateProfile(Customer newProfile) {
        return customerRepository.save(newProfile);
    }

    @CacheEvict(
            value = "customer-cache",
            key = "#id"
    )
    @Override
    public void unregisterCustomer(String id) throws Exception {
        Customer customerExisting = customerRepository.findById(id).orElseThrow(() -> new Exception("Customer not found"));
        customerRepository.delete(customerExisting);
    }
}
