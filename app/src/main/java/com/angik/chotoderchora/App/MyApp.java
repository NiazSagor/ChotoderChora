package com.angik.chotoderchora.App;

import android.app.Application;
import android.content.SharedPreferences;

import com.angik.chotoderchora.Activity.MainActivity;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApp extends Application {

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this);
    }
}
