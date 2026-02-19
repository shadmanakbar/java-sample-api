package com.example.api.service;

import com.example.api.model.User;
import com.example.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    public List<User> findAll() {
        return repository.findAll();
    }

    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    public User create(User user) {
        if (repository.existsByEmail(user.getEmail())) {
            throw new IllegalArgumentException("A user with email '" + user.getEmail() + "' already exists");
        }
        return repository.save(user);
    }

    public Optional<User> update(Long id, User incoming) {
        return repository.findById(id).map(existing -> {
            existing.setName(incoming.getName());
            existing.setPhone(incoming.getPhone());
            existing.setRole(incoming.getRole());
            return repository.save(existing);
        });
    }

    public boolean delete(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
