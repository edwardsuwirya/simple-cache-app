package com.enigmacamp.cacheapp.controller;

import com.enigmacamp.cacheapp.dto.RegisterCustomerRequest;
import com.enigmacamp.cacheapp.dto.UpdateProfileRequest;
import com.enigmacamp.cacheapp.model.Customer;
import com.enigmacamp.cacheapp.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
@ActiveProfiles("test")
class CustomerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    CustomerService customerService;

    @TestConfiguration
    static class CourseConfiguration {
        @Bean
        public ObjectMapper objectMapper() {
            return new ObjectMapper();
        }

        @Bean
        public ModelMapper modelMapper() {
            return new ModelMapper();
        }
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ModelMapper modelMapper;

    @Test
    void createCustomer() throws Exception {
        RegisterCustomerRequest dummyCustomer = new RegisterCustomerRequest("Dummy name", "Dummy address");
        Customer customer = modelMapper.map(dummyCustomer, Customer.class);
        customer.setCustomerId("123");
        when(customerService.registerCustomer(any())).thenReturn(customer);
        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyCustomer)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void getCustomerById() throws Exception {
        Customer customer = new Customer("1", "Dummy User", "Dummy Address");
        when(customerService.findCustomerById("1")).thenReturn(customer);
        mockMvc.perform(get("/customers/{id}", "1"))
                .andExpect(status().isOk()).andExpect(content().json(objectMapper.writeValueAsString(customer)));
    }

    @Test
    void updateCustomerProfile() throws Exception {
        UpdateProfileRequest dummyCustomer = new UpdateProfileRequest("1", "Dummy address update");
        Customer customer = modelMapper.map(dummyCustomer, Customer.class);
        when(customerService.updateProfile(any())).thenReturn(customer);
        MvcResult mvcResult = mockMvc.perform(put("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dummyCustomer)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(customer)))
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        assertEquals(actualResponseBody, objectMapper.writeValueAsString(customer));
    }

    @Test
    void deleteBook() throws Exception {
        doNothing().when(customerService).unregisterCustomer("1");
        mockMvc.perform(delete("/customers/{id}", "1"))
                .andExpect(status().isOk()).andExpect(content().string("Success Delete"));
    }
}