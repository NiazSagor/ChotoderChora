package com.angik.chotoderchora.ChoraActivityFragment;

import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.angik.chotoderchora.R;
import com.angik.chotoderchora.databinding.FragmentChoraBinding;
import com.google.android.gms.ads.AdRequest;

public class ChoraFragment extends Fragment {

    private int CURRENT_POSITION = 0;
    private int switchNumber = 0;

    private Handler handler;
    private Runnable runnable;

    private MediaPlayer mediaPlayer;

    public ChoraFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            CURRENT_POSITION = getArguments().getInt("current_index");
        }

        handler = new Handler();

        mediaPlayer = MediaPlayer.create(getContext(), R.raw.test);
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    private FragmentChoraBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChoraBinding.inflate(getLayoutInflater());

        binding.audioSeekBar.setMax(mediaPlayer.getDuration());
        playCycle();

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadBannerAd();
        binding.currentPositionText.setText(String.valueOf(CURRENT_POSITION));
        binding.playPause.setOnClickListener(playButtonClickListener);
        binding.audioSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaPlayer.start();
        playCycle();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.release();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mediaPlayer.release();
    }

    private void playCycle() {
        binding.audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());

        if (mediaPlayer.isPlaying()) {
            runnable = new Runnable() {
                @Override
                public void run() {
                    playCycle();
                }
            };
            handler.postDelayed(runnable, 1000);
        }
    }

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
    }


    private final View.OnClickListener playButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnimatedVectorDrawableCompat drawableCompat;
            AnimatedVectorDrawable animatedVectorDrawable;
            if (switchNumber == 0) {
                binding.playPause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.pause_to_play));
                Drawable drawable = binding.playPause.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();

                } else if (drawable instanceof AnimatedVectorDrawable) {
                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
                mediaPlayer.pause();
                switchNumber++;
            } else {
                binding.playPause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.play_to_pause));
                Drawable drawable = binding.playPause.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();
                } else if (drawable instanceof AnimatedVectorDrawable) {
                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
                mediaPlayer.start();
                switchNumber--;
            }
        }
    };


    private final SeekBar.OnSeekBarChangeListener seekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            if (b) {
                mediaPlayer.seekTo(i);
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    };
}