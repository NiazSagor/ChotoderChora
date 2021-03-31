package com.angik.chotoderchora.ChoraActivityFragment;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.angik.chotoderchora.Activity.ChoraActivity;
import com.angik.chotoderchora.Interface.OnBackPressedListener;
import com.angik.chotoderchora.R;
import com.angik.chotoderchora.databinding.FragmentBanglaNumberBinding;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;

import java.util.Objects;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

@SuppressLint("StaticFieldLeak")
public class BanglaNumberFragment extends Fragment {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    boolean fullscreen = false;
    int switchNumber = 0;

    private ProgressBar progressBar;
    private ImageButton playPause;

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

        playerView = binding.getRoot().findViewById(R.id.video_player_view);
        playerView.setKeepContentOnPlayerReset(true);
        progressBar = playerView.findViewById(R.id.progress_circular);
        playPause = playerView.findViewById(R.id.exo_play_pause);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ((ChoraActivity) Objects.requireNonNull(getActivity())).setOnBackPressedListener(new OnBackPressedListener() {
            @Override
            public void onBackPressed() {
                releasePlayer();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        initializePlayer();
        playPause.setOnClickListener(playButtonClickListener);
    }

    @Override
    public void onResume() {
        super.onResume();
        extractVideo();
    }

    @Override
    public void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(getContext()).build();
        playerView.setPlayer(player);
        playerView.setShutterBackgroundColor(Color.TRANSPARENT);
        player.addListener(playerEventListener);
        player.setPlayWhenReady(playWhenReady);
        player.seekTo(currentWindow, playbackPosition);
    }

    private void releasePlayer() {
        if (player != null) {
            playWhenReady = player.getPlayWhenReady();
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            player.release();
            player = null;
        }
    }

    private final Player.EventListener playerEventListener = new Player.EventListener() {
        @Override
        public void onPlaybackStateChanged(int state) {

            playerView.showController();

            switch (state) {
                case Player.STATE_BUFFERING:
                    progressBar.setVisibility(View.VISIBLE);
                    playPause.setVisibility(View.GONE);
                    break;
                case Player.STATE_READY:
                    progressBar.setVisibility(View.GONE);
                    playerView.showController();
                    playPause.setVisibility(View.VISIBLE);

                    binding.videoFrameLayout.setVisibility(View.VISIBLE);

                    ConstraintLayout parent = binding.getRoot().findViewById(R.id.parentLayout);
                    ConstraintSet set = new ConstraintSet();
                    set.clone(parent);

                    set.constrainWidth(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);
                    set.constrainHeight(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);

                    set.applyTo(parent);

                    break;
                case Player.STATE_ENDED:
                    progressBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_IDLE:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private final View.OnClickListener playButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnimatedVectorDrawableCompat drawableCompat;
            AnimatedVectorDrawable animatedVectorDrawable;
            if (switchNumber == 0) {
                playPause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.pause_to_play));
                Drawable drawable = playPause.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();

                } else if (drawable instanceof AnimatedVectorDrawable) {
                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
                player.pause();
                switchNumber++;
            } else {
                playPause.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.play_to_pause));
                Drawable drawable = playPause.getDrawable();

                if (drawable instanceof AnimatedVectorDrawableCompat) {
                    drawableCompat = (AnimatedVectorDrawableCompat) drawable;
                    drawableCompat.start();
                } else if (drawable instanceof AnimatedVectorDrawable) {
                    animatedVectorDrawable = (AnimatedVectorDrawable) drawable;
                    animatedVectorDrawable.start();
                }
                player.play();
                switchNumber--;
            }
        }
    };

    private void extractVideo() {
        new YouTubeExtractor(getActivity()) {
            @Override
            protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                if (ytFiles != null) {
                    int itag = 22;
                    String downloadUrl = ytFiles.get(itag).getUrl();

                    DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(), getResources().getString(R.string.app_name));
                    MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(downloadUrl));

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            player.prepare(mediaSource);
                        }
                    });
                }
            }
        }.extract("3_HhhOm6XJc", true, false);
    }
}