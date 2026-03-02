package com.example.ordermanagement.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OrderRequestDTO(

        @NotNull
        @PastOrPresent
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate orderDate,

        @NotNull
        @Positive
        BigDecimal totalAmount
) {}
