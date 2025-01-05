package com.example.emptyappwithlogin;

import android.app.Application;

import com.google.firebase.FirebaseApp;

import dagger.hilt.android.HiltAndroidApp;

@HiltAndroidApp
public class EmptyAppWithLoginApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
    }
}
