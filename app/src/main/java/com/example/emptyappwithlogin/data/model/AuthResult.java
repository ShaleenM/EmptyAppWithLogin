package com.example.emptyappwithlogin.data.model;

public class AuthResult {
    private boolean success;
    private String error;
    private User user;

    public AuthResult(boolean success, String error, User user) {
        this.success = success;
        this.error = error;
        this.user = user;
    }

    public static AuthResult success(User user) {
        return new AuthResult(true, null, user);
    }

    public static AuthResult error(String error) {
        return new AuthResult(false, error, null);
    }

    public boolean isSuccess() { return success; }
    public String getError() { return error; }
    public User getUser() { return user; }
} 