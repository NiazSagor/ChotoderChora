package com.angik.chotoderchora.ChoraActivityAdapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.angik.chotoderchora.ChoraActivityFragment.BanglaNumberFragment;
import com.angik.chotoderchora.ChoraActivityFragment.BenjonBornoVideoFragment;

public class VideoViewPagerAdapter extends FragmentStateAdapter {

    private final int TO_WHAT_FRAGMENT;

    public VideoViewPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, int position) {
        super(fragmentManager, lifecycle);
        this.TO_WHAT_FRAGMENT = position;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (TO_WHAT_FRAGMENT == 2) {
            return new BenjonBornoVideoFragment();
        } else if (TO_WHAT_FRAGMENT == 3) {
            return new BanglaNumberFragment();
        }

        return null;
    }

    @Override
    public int getItemCount() {
        return 1;
    }
}