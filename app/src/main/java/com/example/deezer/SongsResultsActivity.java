package com.example.deezer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

import com.example.deezer.members.Artist;
import com.example.root.R;

public class SongsResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_results);

        Intent fromSearch = getIntent();
        Artist artist = (Artist) fromSearch.getSerializableExtra("artist");

        TextView title = findViewById(R.id.songTitle);
        title.setText(artist.getName() + "'s Songs");

    }
}