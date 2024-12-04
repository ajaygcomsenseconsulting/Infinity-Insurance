package com.infinityinsurance.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO class for user registration.
 * It contains fields to capture user details and applies validation constraints.
 */
@Data // Lombok annotation to generate boilerplate code like getters, setters, equals, hashCode, and toString.
public class UserRegistrationDto {

    @NotBlank // Ensures the field is not null or empty after trimming.
    private String fullName; // Full name of the user.

    @NotBlank // Ensures the field is not null or empty after trimming.
    private String username; // Unique username for the user.

    @NotBlank // Ensures the field is not null or empty after trimming.
    @Email // Validates that the field contains a properly formatted email address.
    private String email; // Email address of the user.

    @NotBlank // Ensures the field is not null or empty after trimming.
    @Size(min = 8) // Ensures the password has a minimum length of 8 characters.
    private String password; // Password for the user account.

    @NotBlank // Ensures the field is not null or empty after trimming.
    private String phone; // Phone number of the user.
}
