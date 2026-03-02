package com.example.ordermanagement.dto.response;

public record CustomerResponseDTO(
        Long id,
        String name,
        String email,
        int totalOrders
) {}
