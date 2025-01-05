package com.example.emptyappwithlogin.ui.auth.register;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.emptyappwithlogin.R;
import com.example.emptyappwithlogin.data.model.AuthResult;
import com.example.emptyappwithlogin.ui.home.HomeActivity;
import com.example.emptyappwithlogin.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity handling new user registration.
 * Provides form validation and user registration functionality with optional
 * fields for name and phone number.
 */
@AndroidEntryPoint
public class RegisterActivity extends AppCompatActivity {
    private RegisterViewModel viewModel;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private TextInputEditText nameInput;
    private TextInputEditText phoneInput;
    private View progressBar;

    private static final String TAG = "RegisterActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        // Initialize views
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        nameInput = findViewById(R.id.nameInput);
        phoneInput = findViewById(R.id.phoneInput);
        progressBar = findViewById(R.id.progressBar);

        // Remove commented toolbar code
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register");

        // Setup click listener
        findViewById(R.id.registerButton).setOnClickListener(v -> attemptRegistration(
                emailInput.getText().toString().trim(),
                passwordInput.getText().toString().trim(),
                nameInput.getText().toString().trim(),
                phoneInput.getText().toString().trim()
        ));

        // Observe registration state
        viewModel.getRegisterResult().observe(this, this::handleAuthResult);
    }

    /**
     * Validates and attempts to register a new user with the provided information.
     * @param email User's email address
     * @param password User's password
     * @param name User's name (optional)
     * @param phone User's phone number (optional)
     */
    private void attemptRegistration(String email, String password, String name, String phone) {

        if (!validateInputs(email, password, phone, name)) {
            Log.i(TAG, "Registration Failed: Invalid inputs");
            Toast.makeText(this, "Registration Failed: Invalid Inputs", Toast.LENGTH_SHORT).show();
            return;
        }

        viewModel.register(email, password, name.isEmpty() ? null : name, phone.isEmpty() ? null : phone);
    }

    /**
     * Validates all input fields according to the application's requirements.
     * @param email Email to validate
     * @param password Password to validate
     * @param phone Phone number to validate (optional)
     * @param name Username to validate (optional)
     * @return boolean indicating if all inputs are valid
     */
    private boolean validateInputs(String email, String password, String phone, String name) {
        boolean isValid = true;

        if (!ValidationUtils.isValidEmail(email)) {
            Log.i(TAG, "Registration Failed: Invalid email format");
            emailInput.setError("Invalid email format");
            isValid = false;
        }

        if (!ValidationUtils.isValidPassword(password)) {
            Log.i(TAG, "Registration Failed: Invalid password format");
            passwordInput.setError("Password must be at least 8 characters with letters and numbers");
            isValid = false;
        }

        if (!phone.isEmpty() && !ValidationUtils.isValidPhone(phone)) {
            Log.i(TAG, "Registration Failed: Invalid phone number format");
            phoneInput.setError("Invalid phone number format");
            isValid = false;
        }

        if (!name.isEmpty() && !ValidationUtils.isValidUsername(name)) {
            Log.i(TAG, "Registration Failed: Invalid user name");
            nameInput.setError("Invalid user name");
            isValid = false;
        }

        return isValid;
    }

    private void handleAuthResult(AuthResult result) {
        if (result == null) {
            showLoading(true);
        } else {
            showLoading(false);

            if (result.isSuccess()) {
                showSuccess();
                navigateToHome();
            } else {
                showError(result.getError());
            }
        }
    }

    private void showLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        emailInput.setEnabled(!isLoading);
        passwordInput.setEnabled(!isLoading);
        nameInput.setEnabled(!isLoading);
        phoneInput.setEnabled(!isLoading);
        findViewById(R.id.registerButton).setEnabled(!isLoading);
    }

    private void showSuccess() {
        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private void navigateToHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
