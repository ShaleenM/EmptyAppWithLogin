package com.example.emptyappwithlogin.utils;

import android.util.Patterns;

import javax.annotation.Nullable;

// ValidationUtils.java
public class ValidationUtils {
    private ValidationUtils() {
        // Private constructor to prevent instantiation
    }

    public static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        // Minimum 8 characters, at least one letter and one number
        String passwordPattern = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@#$%^&+=]?).*[A-Za-z\\d@#$%^&+=]{8,}$";
        return password.matches(passwordPattern);
    }

    public static boolean isValidPhone(@Nullable String phone) {
        if (phone == null) return true;
        return phone.matches("^[+]?[0-9]{10,13}$");
    }

    public static boolean isValidUsername(String username) {
        // Username must be 3-20 characters long, start with a letter,
        // and contain only letters, numbers, and underscores
        String usernamePattern = "^[a-zA-Z][a-zA-Z0-9_]{2,19}$";
        return username != null && username.matches(usernamePattern);
    }
}