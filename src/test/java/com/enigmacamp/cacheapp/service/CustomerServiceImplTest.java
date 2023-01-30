package com.enigmacamp.cacheapp.service;

import com.enigmacamp.cacheapp.model.Customer;
import com.enigmacamp.cacheapp.repository.CustomerRepository;
import com.enigmacamp.cacheapp.repository.CustomerRepositoryImpl;
import com.enigmacamp.cacheapp.utils.CacheFactory;
import com.enigmacamp.cacheapp.utils.CustomerCache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerServiceImplTest {
    private CustomerCache customerCache;

    private CustomerService customerService;

    Customer dummyCustomer = new Customer(null, "Dummy customer 1", "Dummy address 1");

    @BeforeEach
    void setup() {
        CacheFactory cacheFactory = new CacheFactory();
        customerCache = new CustomerCache(cacheFactory.getCacheManager(), "Customer-Test-Cache", 1100);
        CustomerRepository repo = new CustomerRepositoryImpl();
        customerService = new CustomerServiceImpl(repo, customerCache);
    }

    @Test
    void whenFindCustomerByIdSecondTime_thenReturnBookFromCache() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        assertFalse(customerCache.getCustomerCache().containsKey("1"));
        customerService.findCustomerById("1");

        assertTrue(customerCache.getCustomerCache().containsKey("1"));
        customerService.findCustomerById("1");
    }

    @Test
    void whenFindCustomerByIdSecondTimeAndCacheIsExpired_thenReturnBookFromRepo() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        assertFalse(customerCache.getCustomerCache().containsKey("1"));
        customerService.findCustomerById("1");

        Thread.sleep(1111);

        assertFalse(customerCache.getCustomerCache().containsKey("1"));
        customerService.findCustomerById("1");
    }

    @Test
    void whenUpdateCustomer_thenCacheShouldUpdated() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        assertFalse(customerCache.getCustomerCache().containsKey("1"));
        Customer customerCacheMiss = customerService.findCustomerById("1");

        customerCacheMiss.setAddress("Update dummy address");
        customerService.updateProfile(customerCacheMiss);

        assertTrue(customerCache.getCustomerCache().containsKey("1"));
        Customer customerCacheHit = customerService.findCustomerById("1");
        assertEquals("Update dummy address", customerCacheHit.getAddress());
    }

    @Test
    void whenDeleteCustomer_thenCacheShouldDeleted() throws Exception {
        customerService.registerCustomer(dummyCustomer);
        assertFalse(customerCache.getCustomerCache().containsKey("1"));
        Customer customerCacheMiss = customerService.findCustomerById("1");

        customerService.unregisterCustomer(customerCacheMiss.getCustomerId());

        assertFalse(customerCache.getCustomerCache().containsKey("1"));
    }
}