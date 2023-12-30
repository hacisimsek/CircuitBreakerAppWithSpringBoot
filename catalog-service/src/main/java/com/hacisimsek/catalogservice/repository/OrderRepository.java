package com.hacisimsek.catalogservice.repository;

import com.hacisimsek.catalogservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCategory(String category);
}
