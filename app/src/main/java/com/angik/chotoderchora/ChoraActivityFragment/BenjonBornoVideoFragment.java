package com.angik.chotoderchora.ChoraActivityFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.angik.chotoderchora.Activity.ChoraActivity;
import com.angik.chotoderchora.Interface.OnBackPressedListener;
import com.angik.chotoderchora.databinding.FragmentVideoBinding;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener;

import java.util.Objects;

public class BenjonBornoVideoFragment extends Fragment {

    private FragmentVideoBinding binding;

    public BenjonBornoVideoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentVideoBinding.inflate(getLayoutInflater());

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
            String videoId = "3_HhhOm6XJc";
            youTubePlayer.loadVideo(videoId, 0);
        }
    };
}