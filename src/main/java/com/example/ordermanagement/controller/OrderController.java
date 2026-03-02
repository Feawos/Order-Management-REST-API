package com.example.ordermanagement.controller;

import com.example.ordermanagement.dto.request.OrderRequestDTO;
import com.example.ordermanagement.dto.request.OrderUpdateDTO;
import com.example.ordermanagement.dto.response.OrderResponseDTO;
import com.example.ordermanagement.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/customers/{customerId}/orders")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> create(
            @PathVariable Long customerId,
            @Valid @RequestBody OrderRequestDTO dto) {

        OrderResponseDTO response = service.createOrder(customerId, dto);

        URI location = URI.create(
                String.format("/customers/%d/orders/%d",
                        customerId, response.orderId())
        );

        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> list(
            @PathVariable Long customerId,
            @PageableDefault(size = 10, sort = "orderDate", direction = Sort.Direction.DESC)
            Pageable pageable) {

        return ResponseEntity.ok(
                service.getOrdersForCustomer(customerId, pageable)
        );
    }

    @PatchMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> update(
            @PathVariable Long customerId,
            @PathVariable Long orderId,
            @Valid @RequestBody OrderUpdateDTO dto) {

        OrderResponseDTO updated =
                service.updateOrder(customerId, orderId, dto);

        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long customerId,
            @PathVariable Long orderId) {

        service.deleteOrder(customerId, orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrderResponseDTO>> filterByDate(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate) {

        return ResponseEntity.ok(
                service.getOrdersBetween(startDate, endDate)
        );
    }
}
