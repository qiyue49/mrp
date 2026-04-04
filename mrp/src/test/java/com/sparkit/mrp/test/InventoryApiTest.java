package com.sparkit.mrp.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class InventoryApiTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    public void testGetAllInventory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void testGetInventoryByMaterialCode() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/inventory/MAT-001"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
