package com.example.turfbooking.Controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.turfbooking.Model.User;
import com.example.turfbooking.Requests.LoginRequest;
import com.example.turfbooking.Requests.RegisterRequest;
import com.example.turfbooking.Requests.UserResponse;
import com.example.turfbooking.Services.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserResponse register(@RequestBody RegisterRequest dto) {
        User user = userService.register(dto);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    @PostMapping("/login")
    public UserResponse login(@RequestBody LoginRequest dto) {
        User user = userService.login(dto);
        return new UserResponse(user.getId(), user.getName(), user.getEmail());
    }

    // ✅ Add this endpoint to fetch all registered users
    @GetMapping("/users")
    public List<UserResponse> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return users.stream()
                .map(user -> new UserResponse(user.getId(), user.getName(), user.getEmail()))
                .collect(Collectors.toList());
    }
}