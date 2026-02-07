package com.example.turfbooking.Services;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.turfbooking.Model.User;
import com.example.turfbooking.Repository.UserRepository;
import com.example.turfbooking.Requests.LoginRequest;
import com.example.turfbooking.Requests.RegisterRequest;

@Service
public class UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;

    // ✅ Constructor Injection
    public UserService(UserRepository userRepo, PasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.encoder = encoder;
    }

    // Register a new user
    public User register(RegisterRequest dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already registered");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(encoder.encode(dto.getPassword()));

        return userRepo.save(user);
    }

    // Login user
    public User login(LoginRequest dto) {

        User user = userRepo.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found. Please sign up!"));

        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return user;
    }

    // ✅ New method to get all users
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }
}
