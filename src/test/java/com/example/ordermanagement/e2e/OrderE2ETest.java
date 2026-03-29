package com.example.ordermanagement.e2e;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class OrderE2ETest {

    @Autowired private MockMvc mockMvc;

    @Test
    void fullFlow_shouldWork() throws Exception {

        String customer = mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"name":"John","email":"john@test.com"}
                """))
                .andReturn().getResponse().getContentAsString();

        Number id = JsonPath.read(customer, "$.id");
        Long customerId = id.longValue();

        mockMvc.perform(post("/customers/" + customerId + "/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {"orderDate":"2026-03-02","totalAmount":100}
                """))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/customers/" + customerId + "/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray());
    }

    @Test
    void invalidOrder_returns400() throws Exception {
        mockMvc.perform(post("/customers/1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
}