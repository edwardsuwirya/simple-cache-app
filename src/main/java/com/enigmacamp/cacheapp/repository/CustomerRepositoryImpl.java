package com.enigmacamp.cacheapp.repository;

import com.enigmacamp.cacheapp.model.Customer;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CustomerRepositoryImpl implements CustomerRepository {
    private final List<Customer> customerList = new ArrayList<>();

    @Override
    public Customer save(Customer newCustomer) {
        if (newCustomer.getCustomerId() == null) {
            newCustomer.setCustomerId(Integer.toString(customerList.size() + 1));
            customerList.add(newCustomer);
            return newCustomer;
        } else {
            Customer customerSelected = null;
            for (Customer customer : customerList) {
                if (customer.getCustomerId().equals(newCustomer.getCustomerId())) {
                    customerSelected = customer;
                    break;
                }
            }
            if (customerSelected != null) {
                customerSelected.setAddress(newCustomer.getAddress());
                customerSelected.setFullName(newCustomer.getFullName());
                return customerSelected;
            } else {
                return null;
            }
        }
    }

    @Override
    public Optional<Customer> findById(String id) {
        Customer customerSelected = null;
        for (Customer customer : customerList) {
            if (customer.getCustomerId().equals(id)) {
                customerSelected = customer;
                break;
            }
        }
        return Optional.ofNullable(customerSelected);
    }

    @Override
    public void delete(Customer customer) {
        Optional<Customer> customerSelected = findById(customer.getCustomerId());
        customerSelected.ifPresent(c -> customerList.remove(c));
    }
}
