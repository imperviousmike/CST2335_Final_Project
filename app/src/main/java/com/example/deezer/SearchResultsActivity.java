package com.example.deezer;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.example.root.R;
import com.google.android.material.navigation.NavigationView;

public class SearchResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}