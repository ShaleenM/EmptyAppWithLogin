package com.example.emptyappwithlogin.ui.auth.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emptyappwithlogin.data.model.AuthResult;
import com.example.emptyappwithlogin.data.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for handling login-related business logic and state management.
 * Manages user authentication and password reset operations.
 */
@HiltViewModel
public class LoginViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<AuthResult> loginState = new MutableLiveData<>();

    @Inject
    public LoginViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    public LiveData<AuthResult> getLoginState() {
        return loginState;
    }

    /**
     * Attempts to log in a user with the provided credentials.
     * @param email User's email address
     * @param password User's password
     * @return boolean indicating if the login attempt was initiated successfully
     */
    public boolean loginUser(String email, String password) {

        authRepository.loginUser(email, password)
            .thenAccept(loginState::setValue)
            .exceptionally(throwable -> {
                loginState.setValue(AuthResult.error(throwable.getMessage()));
                return null;
            });
        return true;
    }

    /**
     * Initiates the password reset process for the given email.
     * @param email Email address for password reset
     * @return boolean indicating if the reset email was sent successfully
     */
    public boolean sendPasswordResetEmail(String email) {
        // TODO: Implement password reset logic
        return true;
    }
}