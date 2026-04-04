package com.sparkit.mrp.data;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.TopicConfig;
import org.apache.rocketmq.remoting.protocol.body.ClusterInfo;
import org.apache.rocketmq.tools.admin.DefaultMQAdminExt;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Component
public class TopicInitializer implements CommandLineRunner {

    @Value("${rocketmq.name-server:localhost:9876}")
    private String nameServer;

    private final List<String> TOPICS = List.of(
            "orders-events",
            "samples-events",
            "production-events",
            "qc-events",
            "logistics-events"
    );

    private final int QUEUE_NUMBERS = 8;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing RocketMQ topics...");
        
        DefaultMQAdminExt admin = new DefaultMQAdminExt();
        try {
            // Set name server
            admin.setNamesrvAddr(nameServer);
            // Start admin client
            admin.start();
            
            // Get broker address from cluster
            String brokerAddr = getBrokerAddress(admin);
            if (brokerAddr == null) {
                log.error("Cannot get broker address from NameServer");
                return;
            }
            
            // Check and create topics
            for (String topic : TOPICS) {
                createTopicIfNotExists(admin, topic, brokerAddr);
            }
            
            log.info("RocketMQ topic initialization completed");
        } catch (Exception e) {
            log.error("Failed to initialize RocketMQ topics", e);
        } finally {
            if (admin != null) {
                admin.shutdown();
            }
        }
    }

    private String getBrokerAddress(DefaultMQAdminExt admin) throws Exception {
        ClusterInfo clusterInfo = admin.examineBrokerClusterInfo();
        Map<String, org.apache.rocketmq.remoting.protocol.route.BrokerData> brokerAddrTable = clusterInfo.getBrokerAddrTable();
        
        if (brokerAddrTable != null && !brokerAddrTable.isEmpty()) {
            Set<String> brokerNames = brokerAddrTable.keySet();
            for (String brokerName : brokerNames) {
                org.apache.rocketmq.remoting.protocol.route.BrokerData brokerData = brokerAddrTable.get(brokerName);
                if (brokerData != null && brokerData.getBrokerAddrs() != null) {
                    Map<Long, String> addrMap = brokerData.getBrokerAddrs();
                    for (Map.Entry<Long, String> entry : addrMap.entrySet()) {
                        if (entry.getValue() != null) {
                            log.info("Found broker: {} at {}", brokerName, entry.getValue());
                            return entry.getValue();
                        }
                    }
                }
            }
        }
        return null;
    }

    private void createTopicIfNotExists(DefaultMQAdminExt admin, String topic, String brokerAddr) throws Exception {
        try {
            // Check if topic exists
            boolean topicExists = false;
            try {
                admin.examineTopicStats(topic);
                topicExists = true;
                log.info("Topic already exists: {}", topic);
            } catch (MQClientException e) {
                if (e.getResponseCode() == 17) {
                    // Topic not found, continue to create
                    log.info("Topic not found, will create: {}", topic);
                } else {
                    throw e;
                }
            }
            
            if (!topicExists) {
                // Create topic - RocketMQ 5.3.3 using TopicConfig
                TopicConfig topicConfig = new TopicConfig();
                topicConfig.setTopicName(topic);
                topicConfig.setReadQueueNums(QUEUE_NUMBERS);
                topicConfig.setWriteQueueNums(QUEUE_NUMBERS);
                topicConfig.setPerm(6);
                
                admin.createAndUpdateTopicConfig(brokerAddr, topicConfig);
                log.info("Created topic: {} with {} queues", topic, QUEUE_NUMBERS);
            }
        } catch (Exception e) {
            log.error("Failed to process topic: {}", topic, e);
        }
    }
}
