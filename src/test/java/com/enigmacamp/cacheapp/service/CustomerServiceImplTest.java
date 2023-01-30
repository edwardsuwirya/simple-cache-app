package com.enigmacamp.cacheapp.service;

import com.enigmacamp.cacheapp.CacheApplication;
import com.enigmacamp.cacheapp.model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(classes = CacheApplication.class)
@ActiveProfiles("test")
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    Customer dummyCustomer;

    @BeforeEach
    void setup() {
        Customer newDummyCustomer = new Customer(null, "Dummy customer 1", "Dummy address 1");
        dummyCustomer = customerService.registerCustomer(newDummyCustomer);
    }

    @Test
    void whenFindCustomerByIdSecondTime_thenReturnBookFromCache() throws Exception {
        Customer customerCacheMiss = customerService.findCustomerById(dummyCustomer.getCustomerId());
        Customer customerCacheHit = customerService.findCustomerById(dummyCustomer.getCustomerId());
        assertEquals(dummyCustomer.getCustomerId(), customerCacheMiss.getCustomerId());
        assertEquals(dummyCustomer.getCustomerId(), customerCacheHit.getCustomerId());
    }

    @Test
    void whenFindCustomerByIdSecondTimeAndCacheIsExpired_thenReturnBookFromRepo() throws Exception {
        Customer customerCacheMiss = customerService.findCustomerById(dummyCustomer.getCustomerId());
        Thread.sleep(1205);
        Customer customerCacheShouldMiss = customerService.findCustomerById(dummyCustomer.getCustomerId());
        assertEquals(dummyCustomer.getCustomerId(), customerCacheMiss.getCustomerId());
        assertEquals(dummyCustomer.getCustomerId(), customerCacheShouldMiss.getCustomerId());
    }

    @Test
    void whenUpdateCustomer_thenCacheShouldUpdated() throws Exception {
        Customer customerCacheMiss = customerService.findCustomerById(dummyCustomer.getCustomerId());
        customerCacheMiss.setAddress("Update dummy address");
        customerService.updateProfile(customerCacheMiss);

        Customer customerCacheHit = customerService.findCustomerById(dummyCustomer.getCustomerId());
        assertEquals("Update dummy address", customerCacheHit.getAddress());
    }

    @Test
    void whenDeleteCustomer_thenCacheShouldDeleted() throws Exception {
        Customer customerCacheMiss = customerService.findCustomerById(dummyCustomer.getCustomerId());
        customerService.unregisterCustomer(customerCacheMiss.getCustomerId());
        assertThrows(Exception.class, () -> customerService.findCustomerById(dummyCustomer.getCustomerId()));
    }
}