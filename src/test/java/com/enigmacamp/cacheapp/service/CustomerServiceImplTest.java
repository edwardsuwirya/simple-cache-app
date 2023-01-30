package com.enigmacamp.cacheapp.service;

import com.enigmacamp.cacheapp.CacheApplication;
import com.enigmacamp.cacheapp.model.Customer;
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

    Customer dummyCustomer = new Customer(null, "Dummy customer 1", "Dummy address 1");

    @Test
    void whenFindCustomerByIdSecondTime_thenReturnBookFromCache() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        Customer customerCacheMiss = customerService.findCustomerById("1");
        Customer customerCacheHit = customerService.findCustomerById("1");
        assertEquals("1", customerCacheMiss.getCustomerId());
        assertEquals("1", customerCacheHit.getCustomerId());
    }

    @Test
    void whenFindCustomerByIdSecondTimeAndCacheIsExpired_thenReturnBookFromRepo() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        Customer customerCacheMiss = customerService.findCustomerById("1");
        Thread.sleep(1205);
        Customer customerCacheShouldMiss = customerService.findCustomerById("1");
        assertEquals("1", customerCacheMiss.getCustomerId());
        assertEquals("1", customerCacheShouldMiss.getCustomerId());
    }

    @Test
    void whenUpdateCustomer_thenCacheShouldUpdated() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        Customer customerCacheMiss = customerService.findCustomerById("1");
        customerCacheMiss.setAddress("Update dummy address");
        customerService.updateProfile(customerCacheMiss);

        Customer customerCacheHit = customerService.findCustomerById("1");
        assertEquals("Update dummy address", customerCacheHit.getAddress());
    }

    @Test
    void whenDeleteCustomer_thenCacheShouldDeleted() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        Customer customerCacheMiss = customerService.findCustomerById("1");
        customerService.unregisterCustomer(customerCacheMiss.getCustomerId());
        assertThrows(Exception.class, () -> customerService.findCustomerById("1"));
    }
}