package com.angik.chotoderchora.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.angik.chotoderchora.ChoraListActivityAdapter.ChoraListAdapter;
import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.R;
import com.angik.chotoderchora.ViewModel.ChoraListActivityViewModel;
import com.angik.chotoderchora.databinding.ActivityChoraListBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class ChoraListActivity extends AppCompatActivity {

    private ActivityChoraListBinding binding;
    private ChoraListActivityViewModel viewModel;

    private InterstitialAd mInterstitialAd;

    private final Handler handler = new Handler();

    private AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoraListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ChoraListActivityViewModel.class);
        }

        adRequest = new AdRequest.Builder().build();

        binding.toolbar.setTitle(this.getResources().getString(R.string.chora));
        binding.toolbar.setNavigationOnClickListener(navigationClickListener);
    }

    @Override
    protected void onStart() {
        super.onStart();

        viewModel.getPoemMutableLiveData().observe(this, getAllPoemsObserver);
    }

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InterstitialAd.load(
                    ChoraListActivity.this, "ca-app-pub-1905179364235619/5542445414", adRequest, interstitialAdLoadCallback
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

    private void waitAndFinishActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 400);
    }

    private final Observer<List<Poem>> getAllPoemsObserver = new Observer<List<Poem>>() {
        @Override
        public void onChanged(List<Poem> poems) {

            if (poems != null) {

                binding.progressCircular.setVisibility(View.GONE);
                ChoraListAdapter adapter = new ChoraListAdapter(ChoraListActivity.this, poems);
                binding.choraRecyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new ChoraListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {
                        Intent intent = new Intent(ChoraListActivity.this, ChoraActivity.class);
                        intent.putExtra("selected_index", position);
                        intent.putExtra("poem_code", poems.get(position).getPoemCode());
                        intent.putExtra("poem_image_link", poems.get(position).getPoemImage());
                        startActivity(intent);
                    }
                });
            }

        }
    };
}