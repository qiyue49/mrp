package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.*;
import com.sparkit.mrp.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MrpService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private BOMRepository bomRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public void runMrpCalculation() {
        log.info("Running MRP calculation...");

        // Get all sales orders
        List<Order> orders = orderRepository.findAll();

        for (Order order : orders) {
            if ("CREATED".equals(order.getStatus())) {
                log.info("Processing order: {}", order.getOrderCode());
                processOrder(order);
            }
        }

        log.info("MRP calculation completed");
    }

    private void processOrder(Order order) {
        // Get BOM for the product
        List<BOM> boms = bomRepository.findByProductCode(order.getDesignId());

        for (BOM bom : boms) {
            // Calculate required quantity
            double requiredQuantity = bom.getQuantity() * order.getQuantity();

            // Check current inventory
            Inventory inventory = inventoryRepository.findByMaterialCode(bom.getMaterialCode());
            double availableQuantity = inventory != null ? inventory.getQuantity() : 0;

            // Calculate shortage
            double shortage = requiredQuantity - availableQuantity;

            if (shortage > 0) {
                log.info("Material {} shortage: {} {}", bom.getMaterialCode(), shortage, bom.getUnit());
                // Here you would create a purchase requisition or production order
            } else {
                log.info("Material {} has sufficient inventory", bom.getMaterialCode());
            }
        }

        // Update order status
        order.setStatus("PROCESSED");
        orderRepository.save(order);
    }
}
