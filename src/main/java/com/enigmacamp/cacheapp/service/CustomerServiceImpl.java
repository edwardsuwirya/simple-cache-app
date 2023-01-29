package com.enigmacamp.cacheapp.service;

import com.enigmacamp.cacheapp.model.Customer;
import com.enigmacamp.cacheapp.repository.CustomerRepository;
import com.enigmacamp.cacheapp.utils.CustomerCache;

public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerCache cache;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerCache cache) {
        this.customerRepository = customerRepository;
        this.cache = cache;
    }

    @Override
    public Customer registerCustomer(Customer newCustomer) {
        return customerRepository.save(newCustomer);
    }

    @Override
    public Customer findCustomerById(String id) throws Exception {
        if (cache.getCustomerCache().containsKey(id)) {
            System.out.println("Customer " + id + " hit");
            return cache.getCustomerCache().get(id);
        }
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new Exception("Customer not found"));
        cache.getCustomerCache().putIfAbsent(customer.getCustomerId(), customer);
        return customer;
    }

    @Override
    public Customer updateProfile(Customer newProfile) throws Exception {
        Customer customerExisting = customerRepository.findById(newProfile.getCustomerId()).orElseThrow(() -> new Exception("Customer not found"));
        Customer customer = customerRepository.save(customerExisting);
        cache.getCustomerCache().replace(customer.getCustomerId(), customer);
        return customer;
    }

    @Override
    public void unregisterCustomer(String id) throws Exception {
        Customer customerExisting = customerRepository.findById(id).orElseThrow(() -> new Exception("Customer not found"));
        cache.getCustomerCache().remove(id);
        customerRepository.delete(customerExisting);
    }
}
