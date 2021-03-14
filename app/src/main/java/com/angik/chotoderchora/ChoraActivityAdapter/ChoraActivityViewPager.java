package com.angik.chotoderchora.ChoraActivityAdapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.angik.chotoderchora.ChoraActivityFragment.ChoraFragment;
import com.angik.chotoderchora.Model.Poem;
import com.angik.chotoderchora.Utility.StringUtility;

import java.util.List;

public class ChoraActivityViewPager extends FragmentStateAdapter {


    private final List<Poem> poemList;

    public ChoraActivityViewPager(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, List<Poem> poemList) {
        super(fragmentManager, lifecycle);
        this.poemList = poemList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Bundle bundle = new Bundle();
        int current = position++;
        bundle.putInt("current_index", current);
        bundle.putString("poem_code", poemList.get(current).getPoemCode());
        bundle.putString("poem_image", poemList.get(current).getPoemImage());
        Fragment choraFragment = new ChoraFragment();
        choraFragment.setArguments(bundle);
        return choraFragment;
    }

    @Override
    public int getItemCount() {
        return poemList.size();
    }
}