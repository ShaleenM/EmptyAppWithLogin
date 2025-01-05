package com.example.emptyappwithlogin.data.model;

import javax.annotation.Nullable;


public class User {
    private String userId;
    private String name;
    private String email;

    private String phoneNumber;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String userId, String email, String name, String phoneNumber) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public static class Builder {
        private final String uid;
        private final String email;
        private String name;
        private String phoneNumber;

        public Builder(String uid, String email) {
            this.uid = uid;
            this.email = email;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public User build() {
            return new User(uid, email, name, phoneNumber);
        }
    }
}