package com.example.productapi.controller;

import com.example.productapi.model.CustomerOrder;
import com.example.productapi.model.OrderStatus;
import com.example.productapi.service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@CrossOrigin("*")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @PostMapping
    public CustomerOrder create(@RequestBody CustomerOrder order) {
        return service.createOrder(order);
    }

    @GetMapping
    public List<CustomerOrder> getAll() {
        return service.getAllOrders();
    }

    @PutMapping("/{id}")
    public CustomerOrder updateStatus(@PathVariable Long id,
                              @RequestParam OrderStatus status) {
        return service.updateStatus(id, status);
    }
}