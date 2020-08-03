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
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
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

import com.example.deezer.members.Artist;
import com.example.root.R;
import com.google.android.material.navigation.NavigationView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private List<Artist> artistList;
    private List<Bitmap> artistPictures;
    private ArtistListAdapter myAdapter;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        artistList = new ArrayList<>();
        artistPictures = new ArrayList<>();
        Intent fromDeezer = getIntent();
        String search = fromDeezer.getStringExtra("search");
        String url = String.format("https://api.deezer.com/search/artist/?q=%s&output=xml", search);

        Toolbar tBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(tBar);

        ListView list = findViewById(R.id.results);
        list.setAdapter(myAdapter = new ArtistListAdapter());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ArtistQuery req = new ArtistQuery();
        req.execute(url);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent gotoResults = new Intent(SearchResultsActivity.this, SongsResultsActivity.class);
                EditText search = findViewById(R.id.searchText);
                gotoResults.putExtra("artist", artistList.get(position));
                startActivity(gotoResults);
            }
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

        String message = null;

        switch (item.getItemId()) {
            case R.id.search:
                Intent gotoResults = new Intent(SearchResultsActivity.this, DeezerActivity.class);
                startActivity(gotoResults);
                break;

            case R.id.fav:
                Intent gotoFav = new Intent(SearchResultsActivity.this, FavouriteActivity.class);
                startActivity(gotoFav);
                break;

            case R.id.instruc:
                new AlertDialog.Builder(SearchResultsActivity.this)
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
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultsActivity.this);
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

    class ArtistQuery extends AsyncTask<String, Integer, String> {
        Artist artist;

        @Override
        protected String doInBackground(String... args) {
            int progress = 0;
            try {
                URL url = new URL(args[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream response = urlConnection.getInputStream();
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(response, "UTF-8");

                String iconName = null;

                int eventType = xpp.getEventType();

                while (eventType != XmlPullParser.END_DOCUMENT) {

                    if (eventType == XmlPullParser.START_TAG) {
                        //If you get here, then you are pointing at a start tag
                        if (xpp.getName().equals("artist")) {
                            artist = new Artist();
                        } else if (xpp.getName().equals("name")) {
                            eventType = xpp.next();
                            if (eventType == XmlPullParser.TEXT)
                                artist.setName(xpp.getText());
                        } else if (xpp.getName().equals("picture_medium")) {
                            eventType = xpp.next();
                            if (eventType == XmlPullParser.TEXT)
                                artist.setPicture(xpp.getText());
                        } else if (xpp.getName().equals("tracklist")) {
                            eventType = xpp.next();
                            if (eventType == XmlPullParser.TEXT)
                                artist.setSongs(xpp.getText());
                            artistList.add(artist);
                            publishProgress(++progress);
                        }
                    }
                    eventType = xpp.next();
                }

                for (Artist a : artistList) {
                    String imageName = null;
                    try {
                        URL u = new URL(a.getPicture());
                        imageName = u.getFile().split("/")[4];
                    } catch (MalformedURLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    Bitmap image = null;
                    url = new URL(a.getPicture());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.connect();
                    int responseCode = urlConnection.getResponseCode();
                    if (responseCode == 200) {
                        image = BitmapFactory.decodeStream(urlConnection.getInputStream());
                    }
                    if (fileExistance(imageName)) {
                        FileInputStream fis = null;
                        try {
                            fis = openFileInput(imageName);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        Bitmap bm = BitmapFactory.decodeStream(fis);
                    } else {
                        FileOutputStream outputStream = openFileOutput(imageName, Context.MODE_PRIVATE);
                        image.compress(Bitmap.CompressFormat.JPEG, 80, outputStream);
                        outputStream.flush();
                        outputStream.close();
                    }
                    publishProgress(++progress);
                    artistPictures.add(image);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            publishProgress(100);
            return "Done";
        }

        public boolean fileExistance(String fname) {
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public void onProgressUpdate(Integer... args) {
            progressBar.setProgress(args[0]);
        }

        public void onPostExecute(String fromDoInBackground) {
            //this.cancel(true);
            progressBar.setVisibility(View.INVISIBLE);
            if (artistList.isEmpty()) {
                TextView text = findViewById(R.id.notFound);
                text.setVisibility(View.VISIBLE);
            }
            myAdapter.notifyDataSetChanged();


        }
    }

    private class ArtistListAdapter extends BaseAdapter {

        public int getCount() {
            return artistList.size();
        }

        public Object getItem(int position) {
            return artistList.get(position);
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
            iView.setImageBitmap(artistPictures.get(position));
            return newView;
        }
    }


}