package com.example.emptyappwithlogin.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.android.emptyappwithlogin.R;
import com.example.emptyappwithlogin.ui.auth.login.LoginActivity;
import com.google.android.material.navigation.NavigationView;
import com.example.emptyappwithlogin.data.model.User;

import dagger.hilt.android.AndroidEntryPoint;

/**
 * Main activity that serves as the home screen after user authentication.
 * This activity implements a navigation drawer pattern and displays user information.
 * It handles user logout and navigation between different sections of the app.
 */
@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {
    private HomeViewModel viewModel;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.viewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup hamburger icon
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);

        // Setup navigation header
        View headerView = navigationView.getHeaderView(0);
        TextView nameTextView = headerView.findViewById(R.id.nav_header_name);
        TextView emailTextView = headerView.findViewById(R.id.nav_header_email);

        // Observe current user
        viewModel.getCurrentUser().observe(this, user -> {
            if (user != null) {
                if (user.getName() != null) {
                    nameTextView.setText(user.getName());
                }

                emailTextView.setText(user.getEmail());
            }
        });

        // Setup navigation view listener
        navigationView.setNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_logout) {
                viewModel.logout();
                navigateToLogin();
                return true;
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
