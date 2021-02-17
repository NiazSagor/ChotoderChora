package com.angik.chotoderchora.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.angik.chotoderchora.ChoraListActivityAdapter.ChoraListAdapter;
import com.angik.chotoderchora.R;
import com.angik.chotoderchora.databinding.ActivityChoraListBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ChoraListActivity extends AppCompatActivity {

    private ActivityChoraListBinding binding;
    private ChoraListAdapter adapter;

    private InterstitialAd mInterstitialAd;

    private final Handler handler = new Handler();

    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoraListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adRequest = new AdRequest.Builder().build();

        // TODO: 17/02/2021
        //binding.toolbar.setTitle("");
        binding.toolbar.setNavigationOnClickListener(navigationClickListener);

        adapter = new ChoraListAdapter(this.getResources().getStringArray(R.array.categories));
        binding.choraRecyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(choraListItemClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InterstitialAd.load(
                    ChoraListActivity.this, "ca-app-pub-3940256099942544/1033173712", adRequest, interstitialAdLoadCallback
            );
        }
    };

    private final InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {

        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            // The mInterstitialAd reference will be null until
            // an ad is loaded.
            mInterstitialAd = interstitialAd;

            if (mInterstitialAd != null) {
                mInterstitialAd.show(ChoraListActivity.this);
                mInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
            } else {
                Toast.makeText(ChoraListActivity.this, "Test ad was not ready yet", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            // Handle the error
            mInterstitialAd = null;
        }
    };

    private final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when fullscreen content is dismissed.
            waitAndFinishActivity();
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            // Called when fullscreen content failed to show.
        }

        @Override
        public void onAdShowedFullScreenContent() {
            // Called when fullscreen content is shown.
            // Make sure to set your reference to null so you don't
            // show it a second time.
            mInterstitialAd = null;
        }
    };

    private final ChoraListAdapter.OnItemClickListener choraListItemClickListener = new ChoraListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            Intent intent = new Intent(ChoraListActivity.this, ChoraActivity.class);
            intent.putExtra("selected_index", position);
            startActivity(intent);
        }
    };

    private void waitAndFinishActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 400);
    }
}