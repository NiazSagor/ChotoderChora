package com.angik.chotoderchora.ChoraActivityAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.angik.chotoderchora.ChoraActivityFragment.AlphabetFragment;
import com.angik.chotoderchora.ChoraActivityFragment.ChoraFragment;
import com.angik.chotoderchora.Model.Poem;

import java.util.List;

public class AlphabetViewPagerAdapter extends FragmentStateAdapter {


    private final List<String> alphabetUrls;

    public AlphabetViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<String> alphabetUrls) {
        super(fragmentManager, lifecycle);
        this.alphabetUrls = alphabetUrls;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        int current = position++;
        bundle.putInt("current_index", position);
        bundle.putString("alphabet_url", alphabetUrls.get(current));
        Fragment alphabetFragment = new AlphabetFragment();
        alphabetFragment.setArguments(bundle);
        return alphabetFragment;
    }

    @Override
    public int getItemCount() {
        return alphabetUrls.size();
    }
}