package com.example.ordermanagement.controller;

import com.example.ordermanagement.domain.Customer;
import com.example.ordermanagement.dto.request.CustomerRequestDTO;
import com.example.ordermanagement.dto.response.CustomerResponseDTO;
import com.example.ordermanagement.exception.ResourceNotFoundException;
import com.example.ordermanagement.mapper.CustomerMapper;
import com.example.ordermanagement.repository.CustomerRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerController(CustomerRepository repository,
                              CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostMapping
    public ResponseEntity<CustomerResponseDTO> create(
            @Valid @RequestBody CustomerRequestDTO dto) {

        Customer saved = repository.save(mapper.toEntity(dto));

        return ResponseEntity
                .created(URI.create("/customers/" + saved.getId()))
                .body(mapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponseDTO> get(@PathVariable Long id) {

        return repository.findById(id)
                .map(mapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + id)
                );
    }
}
