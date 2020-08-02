package com.example.root;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.deezer.DeezerActivity;


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

        geoButton.setOnClickListener(a -> Toast.makeText(this, getResources().getString(R.string.root_toast_message),Toast.LENGTH_LONG).show());
        soccerButton.setOnClickListener(a -> Toast.makeText(this, getResources().getString(R.string.root_toast_message),Toast.LENGTH_LONG).show());
        lyricButton.setOnClickListener(a -> Toast.makeText(this, getResources().getString(R.string.root_toast_message),Toast.LENGTH_LONG).show());
        deezerButton.setOnClickListener(a -> {
            Intent goToDeezer= new Intent(LaunchActivity.this, DeezerActivity.class);
            startActivity(goToDeezer);
        });

    }


}