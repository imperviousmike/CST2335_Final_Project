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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.deezer.database.FavSongDB;
import com.example.deezer.members.Song;
import com.example.root.R;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private List<Song> songList = new ArrayList<>();
    private FavSongDB songDB;
    private List<Bitmap> coverList = new ArrayList<>();
    private FavouriteAdapter adapter;
    private DetailsFragment dFragment;
    private ListView list;
    boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        isTablet = findViewById(R.id.frame) != null;

        songDB = new FavSongDB(this);
        songDB.getWritableDatabase();
        songList = songDB.getAll();

        SongQuery req = new FavouriteActivity.SongQuery();
        req.execute();

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
                Intent gotoResults = new Intent(FavouriteActivity.this, DeezerActivity.class);
                startActivity(gotoResults);
                break;
            case R.id.fav:
                Intent gotoFav = new Intent(FavouriteActivity.this, FavouriteActivity.class);
                startActivity(gotoFav);
                break;
            case R.id.instruc:
                new AlertDialog.Builder(FavouriteActivity.this)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(FavouriteActivity.this);
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

    private class FavouriteAdapter extends BaseAdapter {

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
            iView.setImageBitmap(coverList.get(position));
            return newView;
        }
    }

    class SongQuery extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... args) {

            for (Song s : songList) {
                String imageName = null;
                try {
                    URL u = new URL(s.getAlbumCover());
                    imageName = u.getFile().split("/")[4];
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    Bitmap image = null;
                    URL url = new URL(s.getAlbumCover());
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
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

                    coverList.add(image);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return "Done";
        }

        @Override
        protected void onPostExecute(String result) {
            list = findViewById(R.id.favSongs);
            list.setAdapter(adapter = new FavouriteAdapter());

            list.setOnItemClickListener((parent, view, position, id) -> {
                Bundle dataToPass = new Bundle();
                dataToPass.putSerializable("song", songList.get(position));
                dataToPass.putParcelable("cover", coverList.get(position));

                if (isTablet) {
                    dFragment = new DetailsFragment();
                    dFragment.setArguments(dataToPass);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.frame, dFragment)
                            .commit();
                } else {
                    Intent nextActivity = new Intent(FavouriteActivity.this, EmptyActivity.class);
                    nextActivity.putExtras(dataToPass);
                    startActivity(nextActivity);
                }
            });

            list.setOnItemLongClickListener((parent, view, position, id) -> {
                new AlertDialog.Builder(FavouriteActivity.this)
                        .setTitle(getResources().getString(R.string.favourite_deleteheader))
                        .setMessage(getResources().getString(R.string.favourite_deletemsg) + " " + songList.get(position).getTitle() + "?")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                            if (songDB.deleteMessage(songList.get(position))) {
                                Snackbar.make(list, R.string.favourite_deletesuccess, Snackbar.LENGTH_SHORT)
                                        .show();
                            }
                            coverList.remove(position);
                            songList = songDB.getAll();
                            adapter.notifyDataSetChanged();
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .show();
                return true;
            });


        }


    }


}