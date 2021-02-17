package com.angik.chotoderchora.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.angik.chotoderchora.ChoraActivityAdapter.ChoraActivityViewPager;
import com.angik.chotoderchora.databinding.ActivityChoraBinding;

public class ChoraActivity extends AppCompatActivity {

    private ActivityChoraBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChoraBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // TODO: 17/02/2021
        //binding.toolbar.setTitle("");

        setupViewPager();
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.toolbar.setNavigationOnClickListener(navigationClickListener);
        changeCurrentPage(getIntent().getIntExtra("selected_index", 0));
    }

    private void changeCurrentPage(int current_index) {
        binding.choraViewPager.setCurrentItem(current_index, true);
    }

    protected void setupViewPager() {
        ChoraActivityViewPager adapter = new ChoraActivityViewPager(getSupportFragmentManager(), getLifecycle());

        binding.choraViewPager.setUserInputEnabled(true);

        binding.choraViewPager.setOffscreenPageLimit(ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT);

        binding.choraViewPager.setAdapter(adapter);
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
}