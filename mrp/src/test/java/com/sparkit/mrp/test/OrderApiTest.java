package com.sparkit.mrp.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class OrderApiTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    public void testCreateOrder() throws Exception {
        String orderJson = "{\"orderCode\": \"ORD-003\", \"customerId\": \"CUST-003\", \"designId\": \"PROD-001\", \"quantity\": 15, \"deliveryDate\": \"2026-04-25\", \"status\": \"CREATED\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(orderJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.orderCode").value("ORD-003"))
                .andExpect(jsonPath("$.customerId").value("CUST-003"))
                .andExpect(jsonPath("$.designId").value("PROD-001"))
                .andExpect(jsonPath("$.quantity").value(15))
                .andExpect(jsonPath("$.deliveryDate").value("2026-04-25"))
                .andExpect(jsonPath("$.status").value("CREATED"));
    }

    @Test
    public void testGetAllOrders() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/orders"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
