package com.sparkit.mrp.controller;

import com.sparkit.mrp.dto.OrderDTO;
import com.sparkit.mrp.entity.Order;
import com.sparkit.mrp.service.OrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        Order order = new Order();
        BeanUtils.copyProperties(orderDTO, order);
        return orderService.createOrder(order);
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderService.getOrderById(id);
    }

    @PutMapping("/{id}/status")
    public Order updateOrderStatus(@PathVariable Long id, @RequestParam String status, @RequestParam String updatedBy) {
        return orderService.updateOrderStatus(id, status, updatedBy);
    }

    @GetMapping("/code/{code}")
    public Order getOrderByCode(@PathVariable String code) {
        return orderService.getOrderByCode(code);
    }
}
