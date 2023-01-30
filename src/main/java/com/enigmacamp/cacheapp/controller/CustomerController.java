package com.enigmacamp.cacheapp.controller;

import com.enigmacamp.cacheapp.dto.RegisterCustomerRequest;
import com.enigmacamp.cacheapp.dto.UpdateProfileRequest;
import com.enigmacamp.cacheapp.model.Customer;
import com.enigmacamp.cacheapp.service.CustomerService;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    @Autowired
    ModelMapper modelMapper;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity createCustomer(@RequestBody RegisterCustomerRequest newCustomer) {
        Customer customer = modelMapper.map(newCustomer, Customer.class);
        Customer registerCustomer = customerService.registerCustomer(customer);
        return ResponseEntity.status(HttpStatus.OK).body(registerCustomer);
    }

    @GetMapping("/{id}")
    public ResponseEntity getCustomerById(@PathVariable("id") String id) throws Exception {
        Customer existingCustomer = customerService.findCustomerById(id);
        if (existingCustomer != null) {
            return ResponseEntity.status(HttpStatus.OK).body(existingCustomer);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Customer not found");
        }
    }

    @PutMapping
    public ResponseEntity updateCustomerProfile(@RequestBody UpdateProfileRequest newProfile) throws Exception {
        Customer customer = modelMapper.map(newProfile, Customer.class);
        modelMapper.getConfiguration().setPropertyCondition(Conditions.isNotNull());
        System.out.println(customer.toString());
        Customer updatedCustomer = customerService.updateProfile(customer);
        return ResponseEntity.status(HttpStatus.OK).body(updatedCustomer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteBook(@PathVariable("id") String id) throws Exception {
        customerService.unregisterCustomer(id);
        return ResponseEntity.status(HttpStatus.OK).body("Success Delete");
    }
}
