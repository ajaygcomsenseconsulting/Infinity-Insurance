package com.infinityinsurance.user.mapper;

import com.infinityinsurance.user.dto.UserRegistrationDto;
import com.infinityinsurance.user.entity.User;

/**
 * Mapper utility for converting between UserRegistrationDto and User entity.
 */
public class UserMapper {

    /**
     * Maps UserRegistrationDto to User entity.
     *
     * @param dto the UserRegistrationDto object
     * @return the User entity
     */
    public static User toEntity(UserRegistrationDto dto) {
        if (dto == null) {
            return null;
        }
        
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword());
        user.setPhone(dto.getPhone());
        // isEnabled and createdAt are set with default values in the User entity
        return user;
    }

    /**
     * Maps User entity to UserRegistrationDto.
     *
     * @param user the User entity
     * @return the UserRegistrationDto object
     */
    public static UserRegistrationDto toDto(User user) {
        if (user == null) {
            return null;
        }

        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setFullName(user.getFullName());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setPassword(user.getPassword()); // Consider masking the password or excluding it in real scenarios
        dto.setPhone(user.getPhone());
        return dto;
    }
}
