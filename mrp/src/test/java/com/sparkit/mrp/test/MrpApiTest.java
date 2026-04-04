package com.sparkit.mrp.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class MrpApiTest extends BaseTest {

    @BeforeEach
    public void setup() {
        super.setup();
    }

    @Test
    public void testMrpCalculation() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/mrp/calculate"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
