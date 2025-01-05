package com.example.emptyappwithlogin.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.emptyappwithlogin.data.model.User;
import com.example.emptyappwithlogin.data.repository.AuthRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;

/**
 * ViewModel for the HomeActivity that manages user state and authentication.
 * Handles user logout operations and provides current user information to the UI.
 */
@HiltViewModel
public class HomeViewModel extends ViewModel {
    private AuthRepository authRepository;
    private final MutableLiveData<User> currentUser = new MutableLiveData<>();

    /**
     * Initializes the ViewModel with the auth repository and fetches the current user.
     * @param authRepository Repository handling authentication operations
     */
    @Inject
    public HomeViewModel(AuthRepository authRepository) {
        this.authRepository = authRepository;
        currentUser.setValue(authRepository.getCurrentUser());
    }

    /**
     * Logs out the current user and clears the user state.
     */
    public void logout() {
        this.authRepository.logout();
        currentUser.setValue(null);
    }

    /**
     * Returns the current user as LiveData for UI observation.
     * @return LiveData containing the current user information
     */
    public LiveData<User> getCurrentUser() {
        return currentUser;
    }
} 