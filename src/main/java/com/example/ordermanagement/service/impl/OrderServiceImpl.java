package com.example.ordermanagement.service.impl;

import com.example.ordermanagement.domain.Customer;
import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.dto.request.OrderRequestDTO;
import com.example.ordermanagement.dto.request.OrderUpdateDTO;
import com.example.ordermanagement.dto.response.OrderResponseDTO;
import com.example.ordermanagement.exception.ResourceNotFoundException;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final CustomerRepository customerRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper mapper;

    public OrderServiceImpl(CustomerRepository customerRepository,
                            OrderRepository orderRepository,
                            OrderMapper mapper) {
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public OrderResponseDTO createOrder(Long customerId, OrderRequestDTO dto) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Customer not found with id: " + customerId)
                );

        Order order = mapper.toEntity(dto);
        order.assignCustomer(customer);

        Order saved = orderRepository.save(order);

        return mapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderResponseDTO> getOrdersForCustomer(Long customerId, Pageable pageable) {

        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Customer not found with id: " + customerId);
        }

        return orderRepository
                .findByCustomerId(customerId, pageable)
                .map(mapper::toResponse);
    }

    @Override
    public OrderResponseDTO updateOrder(
            Long customerId,
            Long orderId,
            OrderUpdateDTO dto) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId)
                );

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException(
                    "Order does not belong to customer id: " + customerId
            );
        }

        if (dto.orderDate() != null) {
            order.setOrderDate(dto.orderDate());
        }

        if (dto.totalAmount() != null) {
            order.setTotalAmount(dto.totalAmount());
        }

        return mapper.toResponse(order);
    }

    @Override
    public void deleteOrder(Long customerId, Long orderId) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Order not found with id: " + orderId)
                );

        if (!order.getCustomer().getId().equals(customerId)) {
            throw new ResourceNotFoundException(
                    "Order does not belong to customer id: " + customerId
            );
        }

        orderRepository.delete(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponseDTO> getOrdersBetween(LocalDate start, LocalDate end) {

        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date must be before end date");
        }

        return orderRepository.findByOrderDateBetween(start, end)
                .stream()
                .map(mapper::toResponse)
                .toList();
    }
}
