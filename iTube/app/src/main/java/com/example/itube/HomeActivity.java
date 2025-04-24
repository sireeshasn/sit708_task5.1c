package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private EditText videoUrlEditText;
    private Button playButton, saveButton, myPlaylistButton;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        videoUrlEditText = findViewById(R.id.videoUrlInput);
        playButton = findViewById(R.id.playButton);
        saveButton = findViewById(R.id.saveButton);
        myPlaylistButton = findViewById(R.id.myPlaylistButton);

        // Initialize DB
        dbHelper = new DBHelper(this);

        // Play video
        playButton.setOnClickListener(v -> {
            String url = videoUrlEditText.getText().toString().trim();
            if (!url.isEmpty()) {
                Intent intent = new Intent(HomeActivity.this, VideoActivity.class);
                intent.putExtra("videoUrl", url);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please enter a YouTube URL", Toast.LENGTH_SHORT).show();
            }
        });

        // Add to Playlist
        saveButton.setOnClickListener(v -> {
            String url = videoUrlEditText.getText().toString().trim();
            if (!url.isEmpty()) {
                boolean inserted = dbHelper.insertVideo(url);
                if (inserted) {
                    Toast.makeText(this, "Video added to playlist", Toast.LENGTH_SHORT).show();
                    videoUrlEditText.setText("");
                } else {
                    Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Enter a valid video URL", Toast.LENGTH_SHORT).show();
            }
        });

        // Go to Playlist
        myPlaylistButton.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, MyPlaylistActivity.class);
            startActivity(intent);
        });
    }
}
