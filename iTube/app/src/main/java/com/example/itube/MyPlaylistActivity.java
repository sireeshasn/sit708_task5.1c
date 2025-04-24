package com.example.itube;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MyPlaylistActivity extends AppCompatActivity {

    private ListView playlistListView;
    private DBHelper dbHelper;
    private ArrayList<String> videoUrls;
    private ArrayList<String> displayTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_playlist); // Ensure this file has only a ListView and optional heading

        playlistListView = findViewById(R.id.playlistListView);
        dbHelper = new DBHelper(this);

        videoUrls = new ArrayList<>(dbHelper.getAllPlaylistLinks());
        displayTitles = new ArrayList<>();

        // Generate placeholder display titles like "Video 1", "Video 2", ...
        for (int i = 0; i < videoUrls.size(); i++) {
            displayTitles.add("Video " + (i + 1));
        }

        if (videoUrls.isEmpty()) {
            Toast.makeText(this, "No videos in playlist", Toast.LENGTH_SHORT).show();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.play_list_item,
                R.id.itemText,
                displayTitles
        );

        playlistListView.setAdapter(adapter);

        playlistListView.setOnItemClickListener((parent, view, position, id) -> {
            String url = videoUrls.get(position);
            Intent intent = new Intent(MyPlaylistActivity.this, VideoActivity.class);
            intent.putExtra("videoUrl", url);
            startActivity(intent);
        });
    }
}
