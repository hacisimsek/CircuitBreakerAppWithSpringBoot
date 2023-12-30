package com.hacisimsek.userservice;

import com.hacisimsek.userservice.dto.OrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
@RestController
@RequestMapping("/user-service")
public class UserServiceApplication {

	@Lazy
	@Autowired
	private RestTemplate restTemplate;

	private static final String CATALOG_SERVICE_URL = "http://localhost:9191/orders";

	public static final String SERVICE_SERVICE = "userService";

	private int attempt = 1;

	@GetMapping("/displayOrders")
	@CircuitBreaker(name = SERVICE_SERVICE, fallbackMethod = "hardcodedResponse")
	//@Retry(name = SERVICE_SERVICE, fallbackMethod = "hardcodedResponse")
	public List<OrderDTO> displayOrders(@RequestParam("category") String category){
		String url = category == null ? CATALOG_SERVICE_URL : CATALOG_SERVICE_URL + "/" + category;
		System.out.println("retry method called "+attempt++ +" times "+" at "+new Date());
		return restTemplate.getForObject(url, ArrayList.class);
	}

	public List<OrderDTO> hardcodedResponse(Exception ex){
		return Stream.of(
				new OrderDTO(119, "LED TV", "electronics", "white", 45000),
				new OrderDTO(345, "Headset", "electronics", "black", 7000),
				new OrderDTO(475, "Sound bar", "electronics", "black", 13000),
				new OrderDTO(574, "Puma Shoes", "foot wear", "black & white", 4600),
				new OrderDTO(678, "Vegetable chopper", "kitchen", "blue", 999),
				new OrderDTO(532, "Oven Gloves", "kitchen", "gray", 745)
		).collect(Collectors.toList());
	}

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

}
