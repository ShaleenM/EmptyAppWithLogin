package com.example.emptyappwithlogin.ui.auth.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.autofill.AutofillValue;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.android.emptyappwithlogin.R;
import com.example.emptyappwithlogin.ui.home.HomeActivity;
import com.example.emptyappwithlogin.utils.ValidationUtils;
import com.google.android.material.textfield.TextInputEditText;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Activity handling user login functionality.
 * Provides email/password authentication, remember me functionality,
 * and navigation to registration and password reset.
 */
@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    private CheckBox rememberMeCheckbox;
    private LoginViewModel viewModel;
    private SharedPreferences sharedPreferences;
    private static final String PREF_NAME = "LoginPrefs";
    private static final String KEY_EMAIL = "saved_email";
    private static final String KEY_REMEMBER = "remember_me";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize ViewModel
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);

        rememberMeCheckbox = findViewById(R.id.rememberMeCheckbox);

        findViewById(R.id.forgotPasswordText).setOnClickListener(v -> showForgotPasswordDialog());

        // login
        findViewById(R.id.loginButton).setOnClickListener(v -> attemptLogin());

        // register
        findViewById(R.id.registerText).setOnClickListener(v -> {});

        // Load saved preferences
        loadSavedPreferences();
    }

    /**
     * Loads previously saved login preferences if "Remember Me" was enabled.
     */
    private void loadSavedPreferences() {
        boolean rememberMe = sharedPreferences.getBoolean(KEY_REMEMBER, false);
        if (rememberMe) {
            String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
            findViewById(R.id.emailInput).autofill(AutofillValue.forText(savedEmail));
            rememberMeCheckbox.setChecked(true);
        }
    }

    /**
     * Saves the user's login preferences if "Remember Me" is checked.
     */
    private void saveLoginPreferences() {
        if (rememberMeCheckbox.isChecked()) {
            SharedPreferences.Editor editor = sharedPreferences.edit();

            EditText emailInput = (EditText) findViewById(R.id.emailInput);
            editor.putString(KEY_EMAIL, emailInput.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.apply();
        } else {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
        }
    }

    /**
     * Displays the forgot password dialog and handles password reset requests.
     */
    private void showForgotPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_forgot_password, null);
        TextInputEditText resetEmailInput = dialogView.findViewById(R.id.resetEmailInput);

        builder.setView(dialogView)
                .setTitle("Reset Password")
                .setPositiveButton("Send Reset Link", (dialog, which) -> {
                    String email = resetEmailInput.getText().toString().trim();
                    if (ValidationUtils.isValidEmail(email)) {
                        viewModel.sendPasswordResetEmail(email);
                    } else {
                        Toast.makeText(this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    /**
     * Attempts to log in the user with the provided credentials.
     * Validates inputs and navigates to HomeActivity on success.
     */
    private void attemptLogin() {

        String email = ((EditText) findViewById(R.id.emailInput)).getText().toString().trim();
        String password = ((EditText) findViewById(R.id.passwordInput)).getText().toString().trim();
        boolean loginSuccess = false;
        if ((ValidationUtils.isValidEmail(email) && ValidationUtils.isValidPassword(password))) {
            saveLoginPreferences();
            loginSuccess = viewModel.loginUser(email, password);
        }

        if(loginSuccess) {
            // redirect to home page
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Login failed. Please check your credentials.", Toast.LENGTH_SHORT).show();
        }
    }
}