package com.sparkit.mrp.health;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RocketMQHealthIndicator implements HealthIndicator {

    @Value("${rocketmq.name-server:localhost:9876}")
    private String nameServer;

    @Override
    public Health health() {
        try {
            DefaultMQAdminExt admin = new DefaultMQAdminExt();
            try {
                admin.setNamesrvAddr(nameServer);
                admin.start();
                
                // Test connection by getting broker cluster info
                var clusterInfo = admin.examineBrokerClusterInfo();
                
                return Health.up()
                        .withDetail("nameServer", nameServer)
                        .withDetail("brokerCount", clusterInfo.getBrokerAddrTable().size())
                        .withDetail("clusterCount", clusterInfo.getClusterAddrTable().size())
                        .withDetail("status", "connected")
                        .build();
            } finally {
                if (admin != null) {
                    admin.shutdown();
                }
            }
        } catch (Exception e) {
            log.warn("RocketMQ health check failed: {}", e.getMessage());
            return Health.down()
                    .withDetail("nameServer", nameServer)
                    .withDetail("error", e.getMessage())
                    .withDetail("status", "disconnected")
                    .build();
        }
    }
}
