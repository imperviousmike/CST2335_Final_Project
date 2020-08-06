package com.example.deezer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.root.R;
import com.google.android.material.navigation.NavigationView;


public class DeezerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private final static String SEARCH_KEY = "PreviousSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deezer);

        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.deezer", Context.MODE_PRIVATE);
        String previousSearch = prefs.getString(SEARCH_KEY, "");
        TextView text = findViewById(R.id.previous_search);
        text.setText(previousSearch);

        text.setOnClickListener(click -> {
            Intent gotoResults = new Intent(DeezerActivity.this, SearchResultsActivity.class);
            gotoResults.putExtra("search", text.getText().toString().replaceAll(" ", "_"));
            startActivity(gotoResults);
        });

        Button searchButton = findViewById(R.id.searchButton);

        searchButton.setOnClickListener(click ->
        {
            Intent gotoResults = new Intent(DeezerActivity.this, SearchResultsActivity.class);
            EditText search = findViewById(R.id.searchText);
            gotoResults.putExtra("search", search.getText().toString().replaceAll(" ", "_"));
            startActivity(gotoResults);
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.globe:
                Intent gotoGeo = new Intent(DeezerActivity.this, com.example.geodata.GeoDataActivity.class);
                startActivity(gotoGeo);
                break;
            case R.id.soccer:
                Intent gotoSoccer = new Intent(DeezerActivity.this, com.example.soccer.SoccerActivity.class);
                startActivity(gotoSoccer);
                break;
            case R.id.lyric:
                Intent gotoLyric = new Intent(DeezerActivity.this, com.example.lyrics.LyricsActivity.class);
                startActivity(gotoLyric);
                break;
            case R.id.about:
                Toast.makeText(this, getResources().getString(R.string.toolbar_about_msg), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.search:
                Intent gotoResults = new Intent(DeezerActivity.this, DeezerActivity.class);
                startActivity(gotoResults);
                break;

            case R.id.fav:
                Intent gotoFav = new Intent(DeezerActivity.this, FavouriteActivity.class);
                startActivity(gotoFav);
                break;

            case R.id.instruc:
                new AlertDialog.Builder(DeezerActivity.this)
                        .setTitle(getResources().getString(R.string.nav_instructions))
                        .setMessage(getResources().getString(R.string.nav_instruction_message))
                        .setPositiveButton(android.R.string.no, null)
                        .show();
                break;
            case R.id.aboutAPI:
                String url = "https://developers.deezer.com/guidelines";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.donate:
                AlertDialog.Builder builder = new AlertDialog.Builder(DeezerActivity.this);
                final View customLayout = getLayoutInflater().inflate(R.layout.donate_layout, null);
                builder.setView(customLayout);
                builder.setNegativeButton(android.R.string.no, null);
                builder.setPositiveButton(android.R.string.ok, null);
                builder.show();
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.deezer", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        EditText search = findViewById(R.id.searchText);
        edit.putString(SEARCH_KEY, search.getText().toString());
        edit.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = this.getSharedPreferences(
                "com.example.deezer", Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = prefs.edit();
        EditText search = findViewById(R.id.searchText);
        edit.putString(SEARCH_KEY, search.getText().toString());
        edit.commit();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
