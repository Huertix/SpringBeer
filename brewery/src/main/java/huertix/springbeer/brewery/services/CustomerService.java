package huertix.springbeer.brewery.services;

import huertix.springbeer.brewery.web.model.CustomerDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class CustomerService {

    public CustomerDto getCustomerById(UUID customerId) {
        return CustomerDto.builder().id(UUID.randomUUID())
                .customerName("John")
                .build();
    }

    public CustomerDto savedNewCustomer(CustomerDto customerDto) {
        return CustomerDto.builder()
                .id(UUID.randomUUID())
                .customerName(customerDto.getCustomerName())
                .build();
    }

    public void updateCustomer(UUID customerId, CustomerDto customerDto) {
        log.debug("Updating Customer: " + customerId.toString());
    }

    public void deleteCustomer(UUID customerId) {
        log.debug("Deleting Customer: " + customerId.toString());
    }
}
