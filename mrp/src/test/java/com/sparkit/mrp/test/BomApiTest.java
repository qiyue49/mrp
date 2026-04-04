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
public class BomApiTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    public void testGetAllBoms() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/boms"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testCreateBom() throws Exception {
        String bomJson = "{\"productCode\": \"PROD-002\", \"materialCode\": \"MAT-001\", \"quantity\": 3.0, \"unit\": \"meter\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/boms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(bomJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.productCode").value("PROD-002"))
                .andExpect(jsonPath("$.materialCode").value("MAT-001"))
                .andExpect(jsonPath("$.quantity").value(3.0))
                .andExpect(jsonPath("$.unit").value("meter"));
    }
}
