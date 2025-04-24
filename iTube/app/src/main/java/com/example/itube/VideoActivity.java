package com.example.itube;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video); // ✅ Make sure this file exists

        String videoUrl = getIntent().getStringExtra("videoUrl");
        String videoId = extractVideoId(videoUrl);

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(YouTubePlayer youTubePlayer) {
                if (videoId != null) {
                    youTubePlayer.loadVideo(videoId, 0);
                }
            }
        });
    }

    // Extract the video ID from a YouTube URL
    private String extractVideoId(String url) {
        if (url != null) {
            if (url.contains("v=")) {
                return url.substring(url.indexOf("v=") + 2);
            } else if (url.contains("youtu.be/")) {
                return url.substring(url.lastIndexOf("/") + 1);
            }
        }
        return null;
    }
}
