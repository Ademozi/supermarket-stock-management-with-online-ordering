package com.example.productapi.service;

import com.example.productapi.model.CustomerOrder;
import com.example.productapi.model.OrderStatus;
import com.example.productapi.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private final OrderRepository repo;

    public OrderService(OrderRepository repo){
        this.repo = repo;
    }

    public CustomerOrder createOrder(CustomerOrder order){
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        return repo.save(order);
    }

    public List<CustomerOrder> getAllOrders() {
        return repo.findAll();
    }

    public CustomerOrder updateStatus(Long id, OrderStatus status) {
        CustomerOrder order = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setStatus(status);
        return repo.save(order);
    }
}
