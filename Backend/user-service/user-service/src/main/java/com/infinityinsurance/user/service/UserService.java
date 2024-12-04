package com.infinityinsurance.user.service;

import com.infinityinsurance.user.dto.UserRegistrationDto;
import com.infinityinsurance.user.entity.User;

/**
 * Interface defining user service operations.
 */
public interface UserService {
    void registerUser(UserRegistrationDto userRegistrationDto);
    User getUserById(Long id);
    User getUserByEmail(String email);
    boolean validateCredentials(String username, String password);
}
