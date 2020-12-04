package huertix.springbeer.brewery.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import huertix.springbeer.brewery.services.BeerService;
import huertix.springbeer.brewery.services.CustomerService;
import huertix.springbeer.brewery.web.model.BeerDto;
import huertix.springbeer.brewery.web.model.CustomerDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    CustomerService customerService;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    CustomerDto validCustomer;

    @Before
    public void setUp() {
        validCustomer = CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName("Alan")
                .build();
    }


    @Test
    public void handleGet() throws Exception {
        given(customerService.getCustomerById(any(UUID.class))).willReturn(validCustomer);
        mockMvc.perform(get("/api/v1/customer/" + validCustomer.getId().toString())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(validCustomer.getId().toString()))
                .andExpect(jsonPath("$.customerName").value("Alan"));
    }

    @Test
    public void handlePost() throws Exception {
        CustomerDto customerDto = validCustomer;
        customerDto.setId(null);
        CustomerDto savedDto = CustomerDto.builder().id(UUID.randomUUID()).customerName("Phi;l").build();
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        given(customerService.savedNewCustomer(any())).willReturn(savedDto);

        mockMvc.perform(post("/api/v1/customer/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isCreated());
    }

    @Test
    public void handleUpdate() throws Exception {
        CustomerDto customerDto = validCustomer;
        String customerDtoJson = objectMapper.writeValueAsString(customerDto);

        mockMvc.perform(put("/api/v1/customer/" + validCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isNoContent());

        then(customerService).should().updateCustomer(any(), any());
    }

    @Test
    public void handleDelete() throws Exception {
        String customerDtoJson = "{}";

        mockMvc.perform(delete("/api/v1/customer/" + validCustomer.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(customerDtoJson))
                .andExpect(status().isNoContent());

        then(customerService).should().deleteCustomer(any());
    }
}