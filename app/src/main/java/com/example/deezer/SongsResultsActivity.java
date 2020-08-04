package com.example.deezer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.deezer.database.FavSongDB;
import com.example.deezer.members.Artist;
import com.example.deezer.members.Song;
import com.example.root.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SongsResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<Song> songList;
    private List<Bitmap> albumPictures;
    private List<Song> favList;
    private ProgressBar progressBar;
    private FavSongDB songDB;
    private Artist artist;
    private DetailsFragment dFragment;
    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_songs_results);
        isTablet = findViewById(R.id.frame) != null;
        songList = new ArrayList<>();
        albumPictures = new ArrayList<>();

        songDB = new FavSongDB(this);
        songDB.getWritableDatabase();
        favList = songDB.getAll();

        Intent fromSearch = getIntent();
        artist = (Artist) fromSearch.getSerializableExtra("artist");

        Toolbar tBar = findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        progressBar = findViewById(R.id.songProgress);
        progressBar.setVisibility(View.VISIBLE);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        TextView title = findViewById(R.id.songTitle);
        title.setText(String.format("%s's Songs", artist.getName()));

        SongQuery req = new SongQuery();
        req.execute(artist.getSongs());

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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        switch (item.getItemId()) {
            case R.id.globe:
            case R.id.soccer:
            case R.id.lyric:
                message = getResources().getString(R.string.root_toast_message);
                break;
            case R.id.about:
                message = getResources().getString(R.string.toolbar_about_msg);
                break;
        }
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                Intent gotoResults = new Intent(SongsResultsActivity.this, DeezerActivity.class);
                startActivity(gotoResults);
                break;

            case R.id.fav:
                Intent gotoFav = new Intent(SongsResultsActivity.this, FavouriteActivity.class);
                startActivity(gotoFav);
                break;

            case R.id.instruc:
                new AlertDialog.Builder(SongsResultsActivity.this)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SongsResultsActivity.this);
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

    class SongQuery extends AsyncTask<String, Integer, String> {
        Song song;

        @Override
        protected String doInBackground(String... args) {
            int progress = 0;
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.connect();
                InputStream songListResponse = urlConnection.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(songListResponse, StandardCharsets.UTF_8), 8);
                StringBuilder sb = new StringBuilder();

                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                String result = sb.toString();

                JSONObject songListing = new JSONObject(result);

                JSONArray songListJSON = songListing.getJSONArray("data");

                for (int i = 0; i < songListJSON.length(); i++) {
                    JSONObject songJSON = songListJSON.getJSONObject(i);
                    String songTitle = songJSON.getString("title");
                    String duration = songJSON.getString("duration");
                    JSONObject album = songJSON.getJSONObject("album");
                    String albumName = album.getString("title");
                    String albumCover = album.getString("cover_medium");
                    song = new Song(songTitle, duration, albumName, albumCover, artist.getName());
                    songList.add(song);
                    publishProgress(++progress);
                }

                for (Song s : songList) {
                    String imageName = null;
                    try {
                        URL u = new URL(s.getAlbumCover());
                        imageName = u.getFile().split("/")[4];
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Bitmap image = null;
                    url = new URL(s.getAlbumCover());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    }

                    FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                    if (image != null) {
                        image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                    }
                    outputStream.flush();
                    outputStream.close();

                    publishProgress(++progress);
                    albumPictures.add(image);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress(100);
            return "Done";
        }

        @Override
        public void onProgressUpdate(Integer... args) {
            progressBar.setProgress(args[0]);
        }
        @Override
        public void onPostExecute(String fromDoInBackground) {
            progressBar.setVisibility(View.INVISIBLE);
            if (songList.isEmpty()) {
                TextView text = findViewById(R.id.notFound);
                text.setVisibility(View.VISIBLE);
            }
            ListView list = findViewById(R.id.songs);
            list.setAdapter(new SongListAdapter());

            list.setOnItemClickListener((parent, view, position, id) -> {
                Bundle dataToPass = new Bundle();
                dataToPass.putSerializable("song", songList.get(position));
                dataToPass.putParcelable("cover", albumPictures.get(position));

                if (isTablet) {
                    dFragment = new DetailsFragment();
                    dFragment.setArguments(dataToPass);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, dFragment)
                            .commit();
                } else {
                    Intent nextActivity = new Intent(SongsResultsActivity.this, EmptyActivity.class);
                    nextActivity.putExtras(dataToPass);
                    startActivity(nextActivity);
                }
            });

            list.setOnItemLongClickListener((parent, view, position, id) -> {
                if (!favList.contains(songList.get(position))) {
                    new AlertDialog.Builder(SongsResultsActivity.this)
                            .setTitle(getResources().getString(R.string.favourite_addheader))
                            .setMessage(getResources().getString(R.string.favourite_addmsg) + " " + songList.get(position).getTitle() + " " + getResources().getString(R.string.favourite_addender))
                            .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                                if (songDB.addSong(songList.get(position))) {
                                    Snackbar.make(list, R.string.favourite_addsuccess, Snackbar.LENGTH_SHORT)
                                            .show();
                                }
                                favList = songDB.getAll();
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .show();

                } else {
                    new AlertDialog.Builder(SongsResultsActivity.this)
                            .setTitle(getResources().getString(R.string.favourite_addheader))
                            .setMessage(getResources().getString(R.string.favourite_exists))
                            .setPositiveButton(android.R.string.yes, null)
                            .show();
                }
                return true;
            });


        }
    }

    private class SongListAdapter extends BaseAdapter {

        public int getCount() {
            return songList.size();
        }

        public Object getItem(int position) {
            return songList.get(position);
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View old, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView;
            TextView tView;
            newView = inflater.inflate(R.layout.row_layout, parent, false);
            tView = newView.findViewById(R.id.rowText);
            tView.setText(getItem(position).toString());
            ImageView iView;
            iView = newView.findViewById(R.id.rowImage);
            iView.setImageBitmap(albumPictures.get(position));
            return newView;
        }
    }
}