package com.example.geodata;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.root.R;
import com.google.android.material.navigation.NavigationView;


public class GeoDataActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_data);

        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.geo_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deezer:
                Intent gotoGeo = new Intent(GeoDataActivity.this, com.example.deezer.DeezerActivity.class);
                startActivity(gotoGeo);
                break;
            case R.id.soccer:
                Intent gotoSoccer = new Intent(GeoDataActivity.this, com.example.soccer.SoccerActivity.class);
                startActivity(gotoSoccer);
                break;
            case R.id.lyric:
                Intent gotoLyric = new Intent(GeoDataActivity.this, com.example.lyrics.LyricsActivity.class);
                startActivity(gotoLyric);
                break;
            case R.id.about:
                Toast.makeText(this, getResources().getString(R.string.toolbar_geo_msg), Toast.LENGTH_LONG).show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.instruc:
                new AlertDialog.Builder(GeoDataActivity.this)
                        .setTitle(getResources().getString(R.string.nav_instructions))
                        .setMessage(getResources().getString(R.string.geo_instruction_message))
                        .setPositiveButton(android.R.string.no, null)
                        .show();
                break;
            case R.id.aboutAPI:
                String url = getResources().getString(R.string.geo_api);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;
            case R.id.donate:
                AlertDialog.Builder builder = new AlertDialog.Builder(GeoDataActivity.this);
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
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

}
