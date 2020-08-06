package com.example.root;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.deezer.DeezerActivity;
import com.example.geodata.GeoDataActivity;
import com.example.lyrics.LyricsActivity;
import com.example.soccer.SoccerActivity;


public class LaunchActivity extends AppCompatActivity {
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        Button geoButton = findViewById(R.id.globeButton);
        Button soccerButton = findViewById(R.id.soccerButton);
        Button lyricButton = findViewById(R.id.lyricButton);
        Button deezerButton = findViewById(R.id.deezerButton);

        geoButton.setOnClickListener(a -> {
            Intent goToGeo = new Intent(LaunchActivity.this, GeoDataActivity.class);
            startActivity(goToGeo);
        });
        soccerButton.setOnClickListener(a -> {
            Intent goToSoccer = new Intent(LaunchActivity.this, SoccerActivity.class);
            startActivity(goToSoccer);
        });
        lyricButton.setOnClickListener(a -> {
            Intent gotoLyric = new Intent(LaunchActivity.this, LyricsActivity.class);
            startActivity(gotoLyric);
        });
        deezerButton.setOnClickListener(a -> {
            Intent goToDeezer = new Intent(LaunchActivity.this, DeezerActivity.class);
            startActivity(goToDeezer);
        });

    }


}