package com.angik.chotodergolpo.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.angik.chotodergolpo.Adapter.CategoryListAdapter;
import com.angik.chotodergolpo.databinding.ActivityMainBinding;
import com.google.android.gms.ads.AdRequest;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private CategoryListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new CategoryListAdapter(this);

        binding.categoryGridView.setLayoutManager(
                new GridLayoutManager(getApplicationContext(), 1)
        );
    }

    @Override
    protected void onStart() {
        super.onStart();

        binding.categoryGridView.setAdapter(adapter);
        adapter.setOnItemClickListener(categoryClickListener);

        setUserName();

        loadBannerAd();
    }

    private void setUserName() {
        StringBuilder stringBuilder = new StringBuilder();

//        stringBuilder.append("Hi,").append("\n").append(
//                Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName()
//        );
//
//        binding.greetingTextView.setText(stringBuilder);
//
//        stringBuilder = new StringBuilder();
//        stringBuilder.append("Hi, ").append(
//                FirebaseAuth.getInstance().getCurrentUser().getDisplayName()
//        );

        binding.textView.setText(stringBuilder);
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }

    private final CategoryListAdapter.OnItemClickListener categoryClickListener = new CategoryListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(int position, View view) {
            String[] categoryTitles = {"Category 1", "Category 2", "Category 3", "Category 4"};
            startActivity(
                    new Intent(MainActivity.this, StoryActivity.class).putExtra("category_title", categoryTitles[position])
            );
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    };
}