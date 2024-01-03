package com.hacisimsek.userservice.controller;

import com.hacisimsek.userservice.dto.OrderDTO;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/user-service")
public class UserController {
    @Lazy
    @Autowired
    private RestTemplate restTemplate;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final String CATALOG_SERVICE_URL = "http://localhost:9191/orders";
    public static final String SERVICE_SERVICE = "userService";
    private int attempt = 1;

    @GetMapping("/displayOrders")
    @CircuitBreaker(name = SERVICE_SERVICE, fallbackMethod = "hardcodedResponse")
    //@Retry(name = SERVICE_SERVICE, fallbackMethod = "hardcodedResponse")
    public List<OrderDTO> displayOrders(@RequestParam("category") String category){
        String url = category == null ? CATALOG_SERVICE_URL : CATALOG_SERVICE_URL + "/" + category;
        logger.info("retry method called {} times at {}", attempt++, new Date());
        return restTemplate.getForObject(url, List.class);
    }

    public List<OrderDTO> hardcodedResponse(Exception ex){
        return Stream.of(
                new OrderDTO(119, "LED TV", "electronics", "white", 45000),
                new OrderDTO(345, "Headset", "electronics", "black", 7000),
                new OrderDTO(475, "Sound bar", "electronics", "black", 13000),
                new OrderDTO(574, "Puma Shoes", "foot wear", "black & white", 4600),
                new OrderDTO(678, "Vegetable chopper", "kitchen", "blue", 999),
                new OrderDTO(532, "Oven Gloves", "kitchen", "gray", 745)
        ).toList();
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
