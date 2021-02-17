package com.angik.chotoderchora.ChoraActivityAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.angik.chotoderchora.ChoraActivityFragment.ChoraFragment;
import com.angik.chotoderchora.Utility.StringUtility;

public class ChoraActivityViewPager extends FragmentStateAdapter {


    public ChoraActivityViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        int current = position++;
        bundle.putInt("current_index", current);
        Fragment choraFragment = new ChoraFragment();
        choraFragment.setArguments(bundle);
        return choraFragment;
    }

    @Override
    public int getItemCount() {
        return StringUtility.getDummyCategories().length;
    }
}