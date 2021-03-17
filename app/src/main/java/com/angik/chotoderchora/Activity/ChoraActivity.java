package com.angik.chotoderchora.Activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.angik.chotoderchora.ChoraActivityAdapter.AlphabetViewPagerAdapter;
import com.angik.chotoderchora.ChoraActivityAdapter.ChoraActivityViewPager;
import com.angik.chotoderchora.ChoraActivityAdapter.VideoViewPagerAdapter;
import com.angik.chotoderchora.Helper.FullScreenHelper;
import com.angik.chotoderchora.Interface.OnBackPressedListener;
import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.PageTransform.DepthPageTransformer;
import com.angik.chotoderchora.R;
import com.angik.chotoderchora.ViewModel.ChoraActivityViewModel;
import com.angik.chotoderchora.databinding.ActivityChoraBinding;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

import java.util.List;

public class ChoraActivity extends AppCompatActivity {

    private OnBackPressedListener listener;

    private static final String TAG = "CUSTOM";

    private ActivityChoraBinding binding;

    private ChoraActivityViewModel viewModel;

    private InterstitialAd mInterstitialAd;

    private final Handler handler = new Handler();

    private AdRequest adRequest;

    private FullScreenHelper helper = new FullScreenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ChoraActivityViewModel.class);
        }

        binding.toolbar.setTitle(getIntent().getStringExtra("category_name"));
        binding.toolbar.setNavigationOnClickListener(navigationClickListener);

        adRequest = new AdRequest.Builder().build();

        setupViewPageAndSetCurrentItemToUserSelection();
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadBannerAd();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            helper.enterFullScreen();
            binding.adView.setVisibility(View.GONE);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            helper.exitFullScreen();
            binding.adView.setVisibility(View.VISIBLE);
        }
    }

    protected void setupViewPageAndSetCurrentItemToUserSelection() {
        binding.choraViewPager.setPageTransformer(new DepthPageTransformer());
        if (getIntent().getIntExtra("position", 0) == 0) {
            viewModel.getPoemMutableLiveData().observe(ChoraActivity.this, getAllPoemsObserver);
        } else if (getIntent().getIntExtra("position", 0) == 1) {
            viewModel.getBanglaAlphabetLiveData().observe(ChoraActivity.this, getAlphabetObserver);
        } else if (getIntent().getIntExtra("position", 0) == 2) {
            createBanglaAlphabetVideoFragment();
        } else if (getIntent().getIntExtra("position", 0) == 3) {
            createBanglaNumberVideoFragment();
        }
    }

    private void createBanglaNumberVideoFragment() {
        VideoViewPagerAdapter adapter = new VideoViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), 3);

        binding.choraViewPager.setUserInputEnabled(true);

        binding.choraViewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        binding.choraViewPager.setAdapter(adapter);
    }

    private void createBanglaAlphabetVideoFragment() {
        VideoViewPagerAdapter adapter = new VideoViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), 2);

        binding.choraViewPager.setUserInputEnabled(true);

        binding.choraViewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        binding.choraViewPager.setAdapter(adapter);
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }


    private final Observer<List<Poem>> getAllPoemsObserver = new Observer<List<Poem>>() {
        @Override
        public void onChanged(List<Poem> poems) {

            if (poems != null) {
                ChoraActivityViewPager adapter = new ChoraActivityViewPager(getSupportFragmentManager(), getLifecycle(), poems);

                binding.choraViewPager.setUserInputEnabled(true);

                binding.choraViewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

                binding.choraViewPager.setAdapter(adapter);

                binding.choraViewPager.setCurrentItem(
                        getIntent().getIntExtra("selected_index", 0), false
                );
            }

        }
    };

    private final Observer<List<String>> getAlphabetObserver = new Observer<List<String>>() {
        @Override
        public void onChanged(List<String> strings) {
            if (strings != null) {
                AlphabetViewPagerAdapter adapter = new AlphabetViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), strings);

                binding.choraViewPager.setUserInputEnabled(true);

                binding.choraViewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

                binding.choraViewPager.setAdapter(adapter);
            }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        listener.onBackPressed();
    }

    private void waitAndFinishActivity() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 400);
    }

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            InterstitialAd.load(
                    ChoraActivity.this, "ca-app-pub-1905179364235619/5542445414", adRequest, interstitialAdLoadCallback
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
                mInterstitialAd.show(ChoraActivity.this);
                mInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
            } else {
                ChoraActivity.super.onBackPressed();
            }
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            // Handle the error
            mInterstitialAd = null;
        }
    };

    public void setOnBackPressedListener(OnBackPressedListener listener) {
        this.listener = listener;
    }
}