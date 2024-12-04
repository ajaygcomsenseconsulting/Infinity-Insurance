package com.infinityinsurance.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.infinityinsurance.user.dto.LoginRequest;
import com.infinityinsurance.user.dto.UserRegistrationDto;
import com.infinityinsurance.user.entity.User;
import com.infinityinsurance.user.service.UserService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationDto userRegistrationDto) {
        try {
            userService.registerUser(userRegistrationDto);
            return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/validate")
    public Boolean validateUser(@RequestBody LoginRequest loginRequest) {
        return userService.validateCredentials(loginRequest.getUsername(), loginRequest.getPassword());
    }

    // Endpoint to fetch user by ID
    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    // Endpoint to fetch user by email
    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        try {
            User user = userService.getUserByEmail(email);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}
