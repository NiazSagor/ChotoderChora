package com.angik.chotoderchora.App;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.ads.MobileAds;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApp extends Application {

    private static final String TAG = "MyApp";

    public static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseApp.initializeApp(this);
        MobileAds.initialize(this);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            FirebaseAuth.getInstance().signInAnonymously();
            Log.d(TAG, "onCreate: not null");
        } else {
            Log.d(TAG, "onCreate: null");
        }
    }
}
