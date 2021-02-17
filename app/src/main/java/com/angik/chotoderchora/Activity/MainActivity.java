package com.angik.chotoderchora.Activity;

import android.app.ActivityManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.angik.chotoderchora.MainActivityAdapter.CategoryListAdapter;
import com.angik.chotoderchora.R;
import com.angik.chotoderchora.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private CategoryListAdapter adapter;
    private ActivityMainBinding binding;

    public static final String KEY_IS_FIRST_LAUNCH = "IS_FIRST_LAUNCH";

    private String[] categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        categories = this.getResources().getStringArray(R.array.categories);

        adapter = new CategoryListAdapter(categories);

        binding.categoryGridView.setLayoutManager(
                new GridLayoutManager(getApplicationContext(), 2)
        );

        loadBannerAd();
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.categoryGridView.setAdapter(adapter);
        adapter.setOnItemClickListener(categoryClickListener);

        binding.adView.setAdListener(adListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    private final CategoryListAdapter.OnItemClickListener categoryClickListener = new CategoryListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            if (position == 0) {
                startActivity(
                        new Intent(MainActivity.this, ChoraListActivity.class)
                );
            } else {
                startActivity(
                        new Intent(MainActivity.this, ChoraActivity.class)
                );
            }
        }
    };

    private final AdListener adListener = new AdListener() {
        @Override
        public void onAdLoaded() {
            // Code to be executed when an ad finishes loading.
        }

        @Override
        public void onAdFailedToLoad(LoadAdError adError) {
            // Code to be executed when an ad request fails.
        }

        @Override
        public void onAdOpened() {
            // Code to be executed when an ad opens an overlay that
            // covers the screen.
        }

        @Override
        public void onAdClicked() {
            // Code to be executed when the user clicks on an ad.
        }

        @Override
        public void onAdLeftApplication() {
            // Code to be executed when the user has left the app.
        }

        @Override
        public void onAdClosed() {
            // Code to be executed when the user is about to return
            // to the app after tapping on an ad.
        }
    };
}