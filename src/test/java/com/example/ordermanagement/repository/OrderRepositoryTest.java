package com.example.ordermanagement.repository;

import com.example.ordermanagement.domain.Customer;
import com.example.ordermanagement.domain.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
class OrderRepositoryTest {

    @Autowired private OrderRepository orderRepository;
    @Autowired private CustomerRepository customerRepository;

    @Test
    void saveOrder_success() {
        Customer customer = customerRepository.save(new Customer("John", "j@test.com"));

        Order order = Order.create(customer, LocalDate.now(), BigDecimal.TEN);

        Order saved = orderRepository.save(order);

        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void findByCustomerId_returnsOrders() {
        Customer customer = customerRepository.save(new Customer("John", "j@test.com"));

        Order order = Order.create(customer, LocalDate.now(), BigDecimal.TEN);

        orderRepository.save(order);

        Page<Order> result = orderRepository.findByCustomerId(
                customer.getId(), PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
    }

    @Test
    void pagination_works() {
        Customer customer = customerRepository.save(new Customer("John", "j@test.com"));

        for (int i = 0; i < 15; i++) {
            Order order = Order.create(customer, LocalDate.now(), BigDecimal.TEN);
            orderRepository.save(order);
        }

        Page<Order> page = orderRepository.findByCustomerId(
                customer.getId(), PageRequest.of(0, 10));

        assertThat(page.getContent()).hasSize(10);
    }

    @Test
    void deleteCustomer_cascadesOrders() {
        Customer customer = customerRepository.save(new Customer("John", "j@test.com"));

        Order order = Order.create(customer, LocalDate.now(), BigDecimal.TEN);
        orderRepository.save(order);

        customerRepository.delete(customer);

        assertThat(orderRepository.findAll()).isEmpty();
    }
}
