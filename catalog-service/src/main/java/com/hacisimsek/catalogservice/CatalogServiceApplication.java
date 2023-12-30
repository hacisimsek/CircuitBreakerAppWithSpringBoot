package com.hacisimsek.catalogservice;

import com.hacisimsek.catalogservice.entity.Order;
import com.hacisimsek.catalogservice.repository.OrderRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
@RequestMapping("/orders")
public class CatalogServiceApplication {

	private OrderRepository orderRepository;

	public CatalogServiceApplication(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	@PostConstruct
	public void initOrdersTable(){
		orderRepository.saveAll(
				Stream.of(
						new Order("Mobile", "electronics", "black", 12000),
						new Order("Laptop", "electronics", "white", 45000),
						new Order("Headset", "electronics", "black", 7000),
						new Order("Sound bar", "electronics", "black", 13000),
						new Order("Puma Shoes", "foot wear", "black & white", 4600),
						new Order("Vegetable chopper", "kitchen", "blue", 999),
						new Order("Oven Gloves", "kitchen", "gray", 745),
						new Order("LED TV", "electronics", "white", 45000),
						new Order("Headset", "electronics", "black", 7000)
				).collect(Collectors.toList())
		);
	}

	@GetMapping
	public List<Order> findAll(){
		return orderRepository.findAll();
	}

	@GetMapping("/{category}")
	public List<Order> findByCategory(@PathVariable String category){
		return orderRepository.findByCategory(category);
	}

	public static void main(String[] args) {
		SpringApplication.run(CatalogServiceApplication.class, args);
	}

}
