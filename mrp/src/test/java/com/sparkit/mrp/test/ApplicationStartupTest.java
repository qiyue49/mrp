package com.sparkit.mrp.test;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.yml")
public class ApplicationStartupTest {

    @Test
    public void testApplicationStartsSuccessfully() {
        // If the application starts without throwing exceptions, this test passes
        assertTrue(true, "Application should start successfully");
    }
}
