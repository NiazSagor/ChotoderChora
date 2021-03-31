package com.angik.chotodergolpo.Activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat;

import com.angik.chotodergolpo.Adapter.StoryListAdapter;
import com.angik.chotodergolpo.Model.Story;
import com.angik.chotodergolpo.R;
import com.angik.chotodergolpo.ViewModel.StoryActivityViewModel;
import com.angik.chotodergolpo.databinding.ActivityStoryBinding;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class StoryActivity extends AppCompatActivity {

    private SimpleExoPlayer player;
    private PlayerView playerView;
    private boolean playWhenReady = true;
    private int currentWindow = 0;
    private long playbackPosition = 0;
    boolean fullscreen = false;
    int switchNumber = 0;

    private int CURRENT_INDEX = 0;

    private List<Story> allStoryList = new ArrayList<>();

    private ProgressBar progressBar;
    private ImageButton playPause;
    private AppCompatTextView titleTextView;

    private static final String TAG = "StoryActivityDebug";

    private ActivityStoryBinding binding;

    private StoryActivityViewModel viewModel;

    private StoryListAdapter adapter;

    private InterstitialAd mInterstitialAd;

    private ProgressDialog progressDialog;

    private AdRequest adRequest;

    //private FullScreenHelper helper = new FullScreenHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (viewModel == null) {
            viewModel = ViewModelProviders.of(this).get(StoryActivityViewModel.class);
        }

        playerView = binding.getRoot().findViewById(R.id.video_player_view);
        playerView.setKeepContentOnPlayerReset(true);
        progressBar = playerView.findViewById(R.id.progress_circular);
        playPause = playerView.findViewById(R.id.exo_play_pause);
        titleTextView = playerView.findViewById(R.id.header_tv);

        binding.toolbar.setTitle(getIntent().getStringExtra("category_title"));
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        initializePlayer();

        viewModel.getSelectedVideoUrl().observe(this, selectedVideoUrlObserver);

        viewModel.getAllStories(
                getIntent().getStringExtra("category_title")
        ).observe(this, allStoryObserver);

        loadBannerAd();

        adRequest = new AdRequest.Builder().build();
    }

    @Override
    protected void onResume() {
        super.onResume();
        playPause.setOnClickListener(playButtonClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        releasePlayer();
    }

    private void initializePlayer() {
        player = new SimpleExoPlayer.Builder(this).build();
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

    private void loadBannerAd() {
        AdRequest adRequest = new AdRequest.Builder().build();
        binding.adView.loadAd(adRequest);
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
                    binding.progressBar.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);
                    playerView.showController();
                    playPause.setVisibility(View.VISIBLE);

                    binding.videoFrameLayout.setVisibility(View.VISIBLE);

                    ConstraintLayout parent = findViewById(R.id.parentLayout);
                    ConstraintSet set = new ConstraintSet();
                    set.clone(parent);

                    set.constrainWidth(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);
                    set.constrainHeight(R.id.videoFrameLayout, ConstraintSet.WRAP_CONTENT);

                    set.applyTo(parent);

                    break;
                case Player.STATE_ENDED:
                    playNext();
                    progressBar.setVisibility(View.GONE);
                    break;
                case Player.STATE_IDLE:
                    progressBar.setVisibility(View.GONE);
                    break;
            }
        }
    };

    private void playNext() {
        if (CURRENT_INDEX < adapter.getItemCount() - 1) {
            CURRENT_INDEX++;
            Log.d(TAG, "playNext: ");
            viewModel.getSelectedVideoUrl().setValue(allStoryList.get(CURRENT_INDEX).getStoryVideoUrl());
        } else if (CURRENT_INDEX == adapter.getItemCount() - 1) {
            showInterstitialAd();
        }
    }

    private void showInterstitialAd() {

        progressDialog = ProgressDialog.show(
                StoryActivity.this,
                "Please Wait",
                "Loading Ad",
                false
        );

        InterstitialAd.load(
                StoryActivity.this, "ca-app-pub-1905179364235619/8337887307", adRequest, interstitialAdLoadCallback
        );
    }

    private final InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {

        @Override
        public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
            // The mInterstitialAd reference will be null until
            // an ad is loaded.

            progressDialog.cancel();

            mInterstitialAd = interstitialAd;

            if (mInterstitialAd != null) {
                mInterstitialAd.show(StoryActivity.this);
                mInterstitialAd.setFullScreenContentCallback(fullScreenContentCallback);
            } else {
                Log.d(TAG, "onAdLoaded: not ready yet");
            }
        }

        @Override
        public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
            // Handle the error
            mInterstitialAd = null;
        }
    };

    private final FullScreenContentCallback fullScreenContentCallback = new FullScreenContentCallback() {
        @Override
        public void onAdDismissedFullScreenContent() {
            // Called when fullscreen content is dismissed.
        }

        @Override
        public void onAdFailedToShowFullScreenContent(AdError adError) {
            // Called when fullscreen content failed to show.
        }

        @Override
        public void onAdShowedFullScreenContent() {
            // Called when fullscreen content is shown.
            // Make sure to set your reference to null so you don't
            // show it a second time.
            mInterstitialAd = null;
        }
    };

    private final View.OnClickListener playButtonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            AnimatedVectorDrawableCompat drawableCompat;
            AnimatedVectorDrawable animatedVectorDrawable;
            if (switchNumber == 0) {
                playPause.setImageDrawable(ContextCompat.getDrawable(StoryActivity.this, R.drawable.pause_to_play));
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
                playPause.setImageDrawable(ContextCompat.getDrawable(StoryActivity.this, R.drawable.play_to_pause));
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

    private final Observer<List<Story>> allStoryObserver = new Observer<List<Story>>() {
        @Override
        public void onChanged(List<Story> storyList) {
            if (storyList != null) {

                allStoryList = storyList;

                viewModel.getSelectedVideoUrl().setValue(storyList.get(0).getStoryVideoUrl());

                adapter = new StoryListAdapter(StoryActivity.this, storyList);
                binding.storyRecyclerView.setAdapter(adapter);

                adapter.setOnItemClickListener(new StoryListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position, View view) {

                        CURRENT_INDEX = position;

                        viewModel.getSelectedVideoUrl().setValue(storyList.get(position).getStoryVideoUrl());
                    }
                });
            }
        }
    };

    private final Observer<String> selectedVideoUrlObserver = new Observer<String>() {
        @Override
        @SuppressLint("StaticFieldLeak")
        public void onChanged(String s) {
            if (s != null) {
                if (s != null) {

                    new YouTubeExtractor(StoryActivity.this) {
                        @Override
                        protected void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta videoMeta) {
                            if (ytFiles != null) {
                                int itag = 22;
                                String downloadUrl = ytFiles.get(itag).getUrl();

                                titleTextView.setText(videoMeta.getTitle());

                                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(StoryActivity.this, getResources().getString(R.string.app_name));
                                MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(downloadUrl));

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        player.prepare(mediaSource);
                                    }
                                });
                            } else {
                                Log.d(TAG, "onExtractionComplete: ytfiles is null" + s);
                            }
                        }
                    }.extract(s, true, false);
                }
            }
        }
    };
}