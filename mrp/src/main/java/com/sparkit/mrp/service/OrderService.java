package com.sparkit.mrp.service;

import com.sparkit.mrp.entity.Order;
import com.sparkit.mrp.event.order.OrderCreatedEvent;
import com.sparkit.mrp.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private EventPublisher eventPublisher;

    @Transactional
    public Order createOrder(Order order) {
        order.setStatus("CREATED");
        order.setCreatedAt(LocalDateTime.now());
        
        // 保存订单
        Order savedOrder = orderRepository.save(order);
        log.info("Order created: {}", savedOrder.getId());

        // 发布订单创建事件
        OrderCreatedEvent event = new OrderCreatedEvent();
        event.setEventId(UUID.randomUUID().toString());
        event.setAggregateId(savedOrder.getId().toString());
        event.setVersion(1L);
        event.setSource("order-service");
        event.setOrderId(savedOrder.getId().toString());
        event.setOrderCode(savedOrder.getOrderCode());
        event.setCustomerId(savedOrder.getCustomerId());
        event.setDesignId(savedOrder.getDesignId());
        event.setQuantity(savedOrder.getQuantity());
        event.setDeliveryDate(savedOrder.getDeliveryDate().toString());

        eventPublisher.publishOrderEvent(event);
        log.info("OrderCreatedEvent published: {}", event.getEventId());

        return savedOrder;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public Order getOrderById(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElseThrow(() -> new RuntimeException("Order not found: " + id));
    }

    @Transactional
    public Order updateOrderStatus(Long id, String status, String updatedBy) {
        Order order = getOrderById(id);

        order.setStatus(status);
        order.setUpdatedAt(LocalDateTime.now());

        Order updatedOrder = orderRepository.save(order);
        log.info("Order status updated: {} -> {}", id, status);

        return updatedOrder;
    }

    public Order getOrderByCode(String orderCode) {
        Optional<Order> orderOptional = orderRepository.findByOrderCode(orderCode);
        return orderOptional.orElseThrow(() -> new RuntimeException("Order not found with code: " + orderCode));
    }
}
