package com.infinityinsurance.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.infinityinsurance.user.dto.UserRegistrationDto;
import com.infinityinsurance.user.entity.User;
import com.infinityinsurance.user.mapper.UserMapper;
import com.infinityinsurance.user.repository.UserRepository;
import com.infinityinsurance.user.service.UserService;



/**
 * Implementation of UserService interface.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void registerUser(UserRegistrationDto userRegistrationDto) {
        if (userRepository.existsByEmail(userRegistrationDto.getEmail())) {
            throw new RuntimeException("Email already in use.");
        }

        if (userRepository.existsByUsername(userRegistrationDto.getUsername())) {
            throw new RuntimeException("Username already in use.");
        }

        // Map DTO to Entity
        User user = UserMapper.toEntity(userRegistrationDto);
        
        // Encrypt password before saving
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));

        // Save to database
        userRepository.save(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

	@Override
    public boolean validateCredentials(String username, String password) {
		return userRepository.findByUsername(username)
                .map(user -> passwordEncoder.matches(password, user.getPassword())) // Check password if user exists
                .orElse(false); // Return false if user does not exist
    }
}
