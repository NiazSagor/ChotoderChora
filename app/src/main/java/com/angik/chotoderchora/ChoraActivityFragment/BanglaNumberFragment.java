package com.angik.chotoderchora.ChoraActivityFragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.angik.chotoderchora.Activity.ChoraActivity;
import com.angik.chotoderchora.Interface.OnBackPressedListener;
import com.angik.chotoderchora.R;
import com.angik.chotoderchora.databinding.FragmentBanglaNumberBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BanglaNumberFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BanglaNumberFragment extends Fragment {

    private FragmentBanglaNumberBinding binding;

    public BanglaNumberFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentBanglaNumberBinding.inflate(getLayoutInflater());

        getLifecycle().addObserver(binding.youtubePlayerView);

        binding.youtubePlayerView.addYouTubePlayerListener(youTubePlayerListener);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ChoraActivity) Objects.requireNonNull(getActivity())).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void onBackPressed() {
                binding.youtubePlayerView.release();
            }
        });
    }

    private final AbstractYouTubePlayerListener youTubePlayerListener = new AbstractYouTubePlayerListener() {
        @Override
        public void onReady(YouTubePlayer youTubePlayer) {
            super.onReady(youTubePlayer);
            String videoId = "JuNTqwNOcks";
            youTubePlayer.loadVideo(videoId, 0);
        }
    };
}