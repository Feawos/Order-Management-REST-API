package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.response.OrderResponseDTO;
import com.example.ordermanagement.exception.ResourceNotFoundException;
import com.example.ordermanagement.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private OrderService orderService;

    @Test
    void createOrder_returns201() throws Exception {

        OrderResponseDTO response =
                new OrderResponseDTO(1L, LocalDate.now(), BigDecimal.valueOf(100));

        when(orderService.createOrder(eq(1L), any()))
                .thenReturn(response);

        mockMvc.perform(post("/customers/1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"orderDate":"2026-03-02","totalAmount":100}
                """))
                .andExpect(status().isCreated());
    }

    @Test
    void getOrders_returns200() throws Exception {

        when(orderService.getOrdersForCustomer(eq(1L), any()))
                .thenReturn(Page.empty());

        mockMvc.perform(get("/customers/1/orders"))
                .andExpect(status().isOk());
    }

    @Test
    void getOrder_notFound_returns404() throws Exception {

        when(orderService.getOrderById(1L, 1L))
                .thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/customers/1/orders/1"))
                .andExpect(status().isNotFound());
    }
}
