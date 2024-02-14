package com.ratelimiter.poc.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.*;

@RestController
public class RateLimiterController {

    @RequestMapping("/home")
    public String home() {
        return "Welcome to the home page!";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        return "Login successful for user: " + request.getUsername();
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestBody ForgotPasswordRequest request) {
        return "Password reset instructions sent to your email.";
    }

    @GetMapping("/whitelisted-endpoint")
    public String whitelistedEndpoint() {
        return "This is a whitelisted endpoint.";
    }

    @Data
    static class LoginRequest {
        private String username;
        private String password;
    }

    @Data
    static class ForgotPasswordRequest {
        private String email;
    }
}
