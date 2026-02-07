package com.example.turfbooking.Requests;

public class UserResponse {

    private Long id;
    private String name;
    private String email;

    // ✅ REQUIRED CONSTRUCTOR
    public UserResponse(Long id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    // ✅ GETTERS
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}