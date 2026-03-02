package com.example.ordermanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderUpdateDTO(

        @PastOrPresent(message = "orderDate cannot be in the future")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate orderDate,

        @Positive(message = "totalAmount must be positive")
        BigDecimal totalAmount
) {}
