package com.example.emptyappwithlogin.data.repository;

import com.example.emptyappwithlogin.data.model.AuthResult;
import com.example.emptyappwithlogin.data.model.User;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;

// AuthRepository.java
@Singleton
public class AuthRepository {
    private final FirebaseAuth firebaseAuth;
    private final FirebaseFirestore firestore;

    @Inject
    public AuthRepository(FirebaseAuth firebaseAuth, FirebaseFirestore firestore) {
        this.firebaseAuth = firebaseAuth;
        this.firestore = firestore;
    }

    public CompletableFuture<AuthResult> registerUser(
            String email, 
            String password, 
            @Nullable String name,
            @Nullable String phone) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TaskCompletionSource<AuthResult> taskCompletionSource = new TaskCompletionSource<>();
                
                firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().getUser() != null) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            User user = new User.Builder(firebaseUser.getUid(), email)
                                .name(name)
                                .phoneNumber(phone)
                                .build();

                            // Store additional user data in Firestore
                            firestore.collection("users")
                                .document(firebaseUser.getUid())
                                .set(user)
                                .addOnSuccessListener(aVoid -> 
                                    taskCompletionSource.setResult(AuthResult.success(user)))
                                .addOnFailureListener(e -> 
                                    taskCompletionSource.setResult(AuthResult.error(e.getMessage())));
                        } else {
                            taskCompletionSource.setResult(
                                AuthResult.error("Registration failed")
                            );
                        }
                    });
                return taskCompletionSource.getTask().getResult();
            } catch (Exception e) {
                return AuthResult.error(e.getMessage());
            }
        });
    }

    public CompletableFuture<AuthResult> loginUser(String email, String password) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                TaskCompletionSource<AuthResult> taskCompletionSource = new TaskCompletionSource<>();
                
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && task.getResult().getUser() != null) {
                            FirebaseUser firebaseUser = task.getResult().getUser();
                            
                            firestore.collection("users")
                                .document(firebaseUser.getUid())
                                .get()
                                .addOnSuccessListener(document -> {
                                    User user = document.toObject(User.class);
                                    taskCompletionSource.setResult(AuthResult.success(user));
                                })
                                .addOnFailureListener(e -> 
                                    taskCompletionSource.setResult(AuthResult.error(e.getMessage())));
                        } else {
                            taskCompletionSource.setResult(AuthResult.error("Login failed"));
                        }
                    });
                return taskCompletionSource.getTask().getResult();
            } catch (Exception e) {
                return AuthResult.error(e.getMessage());
            }
        });
    }

    public boolean isUserLoggedIn() {
        return firebaseAuth.getCurrentUser() != null;
    }

    public User getCurrentUser() {
        if (isUserLoggedIn()) {
            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
            return new User.Builder(firebaseUser.getUid(), firebaseUser.getEmail()).build();
        } else {
            return null;
        }
    }

    public void logout() {
        if (isUserLoggedIn()) {
            firebaseAuth.signOut();
        }
    }
}