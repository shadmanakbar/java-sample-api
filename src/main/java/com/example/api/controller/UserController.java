package com.example.api.controller;

import com.example.api.model.User;
import com.example.api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    /** GET /api/users — list all users */
    @GetMapping
    public List<User> list(@RequestParam(required = false) String role) {
        if (role != null && !role.isBlank()) {
            return service.findAll().stream()
                    .filter(u -> u.getRole().equalsIgnoreCase(role))
                    .toList();
        }
        return service.findAll();
    }

    /** GET /api/users/{id} — get user by ID */
    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** POST /api/users — register a new user */
    @PostMapping
    public ResponseEntity<?> create(@Valid @RequestBody User user) {
        try {
            User created = service.create(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", ex.getMessage()));
        }
    }

    /** PUT /api/users/{id} — update user profile */
    @PutMapping("/{id}")
    public ResponseEntity<User> update(
            @PathVariable Long id,
            @Valid @RequestBody User user) {
        return service.update(id, user)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** DELETE /api/users/{id} — remove a user */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        boolean deleted = service.delete(id);
        if (deleted) {
            return ResponseEntity.ok(Map.of("deleted", true, "id", id));
        }
        return ResponseEntity.notFound().build();
    }
}
