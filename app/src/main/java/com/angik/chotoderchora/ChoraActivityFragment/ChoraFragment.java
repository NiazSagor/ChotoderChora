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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.angik.chotoderchora.R;
import com.angik.chotoderchora.ViewModel.ChoraActivityViewModel;
import com.angik.chotoderchora.databinding.FragmentChoraBinding;
import com.bumptech.glide.Glide;

import java.io.IOException;

import static android.view.View.GONE;

public class ChoraFragment extends Fragment {

    private ChoraActivityViewModel viewModel;

    private String POEM_CODE;
    private String POEM_IMAGE_URL;

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
            POEM_IMAGE_URL = getArguments().getString("poem_image");
            POEM_CODE = getArguments().getString("poem_code");
        }

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(ChoraActivityViewModel.class);
        }

        handler = new Handler();

        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);

        playAudio();
    }

    private FragmentChoraBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentChoraBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

        setupPoemBackground();

        binding.playPause.setOnClickListener(playButtonClickListener);
        binding.audioSeekBar.setOnSeekBarChangeListener(seekBarChangeListener);
    }

    private void setupPoemBackground() {

        Glide
                .with(getContext())
                .load(POEM_IMAGE_URL)
                .into(binding.choraImageView);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
            playCycle();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //mediaPlayer.release();
        mediaPlayer.reset();
        handler.removeCallbacks(runnable);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mediaPlayer.reset();
        //mediaPlayer.release();
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

    private void playAudio() {

        viewModel.getPoemAudioLinkMutableLiveData(POEM_CODE).observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                try {
                    mediaPlayer.setDataSource(s);

                    mediaPlayer.prepareAsync();

                    mediaPlayer.setOnPreparedListener(onPreparedListener);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private final MediaPlayer.OnPreparedListener onPreparedListener = new MediaPlayer.OnPreparedListener() {
        @Override
        public void onPrepared(MediaPlayer mediaPlayer) {
            binding.progressCircular.setVisibility(GONE);
            mediaPlayer.start();
            playCycle();
            binding.audioSeekBar.setMax(mediaPlayer.getDuration());
            binding.audioSeekBar.setProgress(mediaPlayer.getCurrentPosition());
        }
    };
}