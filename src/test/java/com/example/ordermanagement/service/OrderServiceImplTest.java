package com.example.ordermanagement.service;

import com.example.ordermanagement.domain.Customer;
import com.example.ordermanagement.domain.Order;
import com.example.ordermanagement.dto.request.OrderRequestDTO;
import com.example.ordermanagement.dto.request.OrderUpdateDTO;
import com.example.ordermanagement.dto.response.OrderResponseDTO;
import com.example.ordermanagement.exception.BusinessException;
import com.example.ordermanagement.exception.ResourceNotFoundException;
import com.example.ordermanagement.mapper.OrderMapper;
import com.example.ordermanagement.repository.CustomerRepository;
import com.example.ordermanagement.repository.OrderRepository;
import com.example.ordermanagement.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock private OrderRepository orderRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private OrderMapper mapper;

    @InjectMocks private OrderServiceImpl orderService;

    @Test
    void createOrder_success() {
        Customer customer = mock(Customer.class);

        OrderRequestDTO request =
                new OrderRequestDTO(LocalDate.now(), BigDecimal.valueOf(100));

        Order order = mock(Order.class);
        OrderResponseDTO response =
                new OrderResponseDTO(1L, request.orderDate(), request.totalAmount());

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(orderRepository.save(any(Order.class))).thenReturn(order);
        when(mapper.toResponse(order)).thenReturn(response);

        OrderResponseDTO result = orderService.createOrder(1L, request);

        assertThat(result).isNotNull();
    }

    @Test
    void getOrderById_success() {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L); // ✅ used

        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(customer); // ✅ used

        OrderResponseDTO dto =
                new OrderResponseDTO(1L, LocalDate.now(), BigDecimal.TEN);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.toResponse(order)).thenReturn(dto);

        OrderResponseDTO result =
                orderService.getOrderById(1L, 1L);

        assertThat(result).isNotNull();
    }

    @Test
    void getOrderById_wrongCustomer() {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(2L); // mismatch

        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(customer);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() ->
                orderService.getOrderById(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void updateOrder_success() {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);

        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(customer);

        OrderUpdateDTO dto =
                new OrderUpdateDTO(null, BigDecimal.valueOf(50));

        OrderResponseDTO response =
                new OrderResponseDTO(1L, LocalDate.now(), BigDecimal.valueOf(50));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(mapper.toResponse(order)).thenReturn(response);

        OrderResponseDTO result =
                orderService.updateOrder(1L, 1L, dto);

        assertThat(result.totalAmount()).isEqualTo(BigDecimal.valueOf(50));
    }

    @Test
    void updateOrder_wrongCustomer() {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(2L);

        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(customer);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() ->
                orderService.updateOrder(1L, 1L,
                        new OrderUpdateDTO(null, BigDecimal.TEN)))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteOrder_success() {
        Customer customer = mock(Customer.class);
        when(customer.getId()).thenReturn(1L);

        Order order = mock(Order.class);
        when(order.getCustomer()).thenReturn(customer);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        orderService.deleteOrder(1L, 1L);

        verify(orderRepository).delete(order);
    }

    @Test
    void deleteOrder_notFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                orderService.deleteOrder(1L, 1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}