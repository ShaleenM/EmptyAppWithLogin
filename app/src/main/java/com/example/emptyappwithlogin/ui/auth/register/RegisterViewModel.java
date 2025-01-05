package com.example.emptyappwithlogin.ui.auth.register;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emptyappwithlogin.data.model.AuthResult;
import com.example.emptyappwithlogin.data.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for handling user registration logic and state management.
 * Manages the registration process and provides registration status updates.
 */
@HiltViewModel
public class RegisterViewModel extends ViewModel {
    private final AuthRepository authRepository;
    private final MutableLiveData<AuthResult> registerResult = new MutableLiveData<>();

    private final String TAG = this.getClass().getSimpleName();

    @Inject
    public RegisterViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }

    /**
     * Attempts to register a new user with the provided information.
     * @param email User's email address
     * @param password User's password
     * @param username User's name (optional)
     * @param phone User's phone number (optional)
     */
    public void register(String email, String password, String username, String phone) {

        Log.i(TAG, "Registering user with email: " + email + ", username: " + username + ", phone: " + phone);

        authRepository.registerUser(email, password, username, phone)
            .thenAccept(registerResult::postValue)
            .exceptionally(throwable -> {
                Log.e(TAG, "Registration Filed ", throwable);
                registerResult.setValue(AuthResult.error(throwable.getMessage()));
                return null;
            });
    }

    /**
     * Returns the registration result as LiveData for UI observation.
     * @return LiveData containing the registration result
     */
    public LiveData<AuthResult> getRegisterResult() {
        return registerResult;
    }
} 