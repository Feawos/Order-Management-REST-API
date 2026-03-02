package com.example.ordermanagement.service;

import com.example.ordermanagement.dto.request.OrderRequestDTO;
import com.example.ordermanagement.dto.request.OrderUpdateDTO;
import com.example.ordermanagement.dto.response.OrderResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {

    OrderResponseDTO createOrder(Long customerId, OrderRequestDTO dto);

    OrderResponseDTO updateOrder(
            Long customerId,
            Long orderId,
            OrderUpdateDTO dto
    );

    Page<OrderResponseDTO> getOrdersForCustomer(Long customerId, Pageable pageable);

    void deleteOrder(Long customerId, Long orderId);

    List<OrderResponseDTO> getOrdersBetween(LocalDate start, LocalDate end);
}
