package com.example.ordermanagement.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderResponseDTO(
        Long orderId,
        LocalDate orderDate,
        BigDecimal totalAmount
) {}
