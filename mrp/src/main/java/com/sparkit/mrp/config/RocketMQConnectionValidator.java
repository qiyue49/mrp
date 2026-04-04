package com.sparkit.mrp.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Configuration
public class RocketMQConnectionValidator {

    @Value("${rocketmq.name-server:localhost:9876}")
    private String nameServer;

    private static final int MAX_RETRIES = 5;
    private static final long RETRY_DELAY_MS = 3000;

    @PostConstruct
    public void validateRocketMQConnection() {
        log.info("Validating RocketMQ connection...");
        
        int retryCount = 0;
        while (retryCount < MAX_RETRIES) {
            try {
                DefaultMQAdminExt admin = new DefaultMQAdminExt();
                try {
                    admin.setNamesrvAddr(nameServer);
                    admin.start();
                    
                    // Test connection by getting broker cluster info
                    admin.examineBrokerClusterInfo();
                    
                    log.info("RocketMQ connection validated successfully");
                    return;
                } finally {
                    if (admin != null) {
                        admin.shutdown();
                    }
                }
            } catch (Exception e) {
                retryCount++;
                log.warn("RocketMQ connection attempt {} failed: {}", retryCount, e.getMessage());
                
                if (retryCount < MAX_RETRIES) {
                    try {
                        log.info("Retrying in {} ms...", RETRY_DELAY_MS);
                        TimeUnit.MILLISECONDS.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } else {
                    log.error("Failed to connect to RocketMQ after {} attempts", MAX_RETRIES);
                    throw new RuntimeException("RocketMQ connection validation failed", e);
                }
            }
        }
    }
}
