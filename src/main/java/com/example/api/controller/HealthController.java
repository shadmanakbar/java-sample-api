package com.example.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
public class HealthController {

    /** GET / — root endpoint, always returns 200 */
    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
                "status", "UP",
                "service", "java-sample-api",
                "timestamp", LocalDateTime.now().toString());
    }

    /** GET /health — simple health check */
    @GetMapping("/health")
    public Map<String, String> health() {
        return Map.of("status", "UP");
    }
}
