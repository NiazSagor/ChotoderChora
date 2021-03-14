package com.angik.chotoderchora.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager2.widget.ViewPager2;

import com.angik.chotoderchora.ChoraActivityAdapter.ChoraActivityViewPager;
import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.PageTransform.DepthPageTransformer;
import com.angik.chotoderchora.ViewModel.ChoraActivityViewModel;
import com.angik.chotoderchora.databinding.ActivityChoraBinding;
import com.google.android.gms.ads.AdRequest;

import java.util.List;

public class ChoraActivity extends AppCompatActivity {

    private ActivityChoraBinding binding;

    private ChoraActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ChoraActivityViewModel.class);
        }

        setupViewPageAndSetCurrentItemToUserSelection();
    }

    @Override
    protected void onStart() {
        super.onStart();

        loadBannerAd();
    }

    protected void setupViewPageAndSetCurrentItemToUserSelection() {
        binding.choraViewPager.setPageTransformer(new DepthPageTransformer());
        viewModel.getPoemMutableLiveData().observe(ChoraActivity.this, getAllPoemsObserver);
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    private final View.OnClickListener navigationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(
                    new Intent(ChoraActivity.this, ChoraListActivity.class)
            );
            finish();
        }
    };

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
}