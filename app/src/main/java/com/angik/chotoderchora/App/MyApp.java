package com.angik.chotoderchora.App;

import android.app.Application;
import android.content.SharedPreferences;

import com.angik.chotoderchora.Activity.MainActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this);

        SharedPreferences sharedPreferences = getSharedPreferences(MainActivity.KEY_IS_FIRST_LAUNCH, MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("isFirstLaunch", true).apply();
    }
}
