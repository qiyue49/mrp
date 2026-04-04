package com.sparkit.mrp.data;

import com.sparkit.mrp.entity.*;
import com.sparkit.mrp.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private BOMRepository bomRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public void run(String... args) throws Exception {
        log.info("Initializing test data...");

        // Initialize materials
        initializeMaterials();

        // Initialize BOMs
        initializeBOMs();

        // Initialize inventory
        initializeInventory();

        // Initialize orders
        initializeOrders();

        log.info("Test data initialization completed");
    }

    private void initializeMaterials() {
        Material material1 = new Material();
        material1.setMaterialCode("MAT-001");
        material1.setMaterialName("Cotton Fabric");
        material1.setCategory("Fabric");
        material1.setUnit("meter");
        material1.setPrice(10.0);
        material1.setLeadTime(7);
        material1.setStatus("ACTIVE");
        materialRepository.save(material1);

        Material material2 = new Material();
        material2.setMaterialCode("MAT-002");
        material2.setMaterialName("Thread");
        material2.setCategory("Accessories");
        material2.setUnit("spool");
        material2.setPrice(5.0);
        material2.setLeadTime(3);
        material2.setStatus("ACTIVE");
        materialRepository.save(material2);

        Material material3 = new Material();
        material3.setMaterialCode("MAT-003");
        material3.setMaterialName("Button");
        material3.setCategory("Accessories");
        material3.setUnit("piece");
        material3.setPrice(0.5);
        material3.setLeadTime(5);
        material3.setStatus("ACTIVE");
        materialRepository.save(material3);
    }

    private void initializeBOMs() {
        BOM bom1 = new BOM();
        bom1.setProductCode("PROD-001");
        bom1.setMaterialCode("MAT-001");
        bom1.setQuantity(2.0);
        bom1.setUnit("meter");
        bomRepository.save(bom1);

        BOM bom2 = new BOM();
        bom2.setProductCode("PROD-001");
        bom2.setMaterialCode("MAT-002");
        bom2.setQuantity(1.0);
        bom2.setUnit("spool");
        bomRepository.save(bom2);

        BOM bom3 = new BOM();
        bom3.setProductCode("PROD-001");
        bom3.setMaterialCode("MAT-003");
        bom3.setQuantity(5.0);
        bom3.setUnit("piece");
        bomRepository.save(bom3);
    }

    private void initializeInventory() {
        Inventory inventory1 = new Inventory();
        inventory1.setMaterialCode("MAT-001");
        inventory1.setMaterialName("Cotton Fabric");
        inventory1.setQuantity(100.0);
        inventory1.setUnit("meter");
        inventory1.setCost(10.0);
        inventoryRepository.save(inventory1);

        Inventory inventory2 = new Inventory();
        inventory2.setMaterialCode("MAT-002");
        inventory2.setMaterialName("Thread");
        inventory2.setQuantity(50.0);
        inventory2.setUnit("spool");
        inventory2.setCost(5.0);
        inventoryRepository.save(inventory2);

        Inventory inventory3 = new Inventory();
        inventory3.setMaterialCode("MAT-003");
        inventory3.setMaterialName("Button");
        inventory3.setQuantity(500.0);
        inventory3.setUnit("piece");
        inventory3.setCost(0.5);
        inventoryRepository.save(inventory3);
    }

    private void initializeOrders() {
        Order order1 = new Order();
        order1.setOrderCode("ORD-001");
        order1.setCustomerId("CUST-001");
        order1.setDesignId("PROD-001");
        order1.setQuantity(10);
        order1.setDeliveryDate(LocalDate.now().plusDays(14));
        order1.setStatus("CREATED");
        orderRepository.save(order1);

        Order order2 = new Order();
        order2.setOrderCode("ORD-002");
        order2.setCustomerId("CUST-002");
        order2.setDesignId("PROD-001");
        order2.setQuantity(5);
        order2.setDeliveryDate(LocalDate.now().plusDays(7));
        order2.setStatus("CREATED");
        orderRepository.save(order2);
    }
}
