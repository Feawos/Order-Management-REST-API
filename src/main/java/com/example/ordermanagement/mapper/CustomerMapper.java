package com.example.ordermanagement.mapper;

import com.example.ordermanagement.domain.Customer;
import com.example.ordermanagement.dto.request.CustomerRequestDTO;
import com.example.ordermanagement.dto.response.CustomerResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public Customer toEntity(CustomerRequestDTO dto) {
        return new Customer(dto.name(), dto.email());
    }

    public CustomerResponseDTO toResponse(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getOrders().size()
        );
    }
}
