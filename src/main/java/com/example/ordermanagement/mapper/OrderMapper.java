package com.example.ordermanagement.mapper;

import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.dto.request.OrderRequestDTO;
import com.example.ordermanagement.dto.response.OrderResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    public OrderResponseDTO toResponse(Order order) {
        return new OrderResponseDTO(
                order.getId(),
                order.getOrderDate(),
                order.getTotalAmount()
        );
    }
}
