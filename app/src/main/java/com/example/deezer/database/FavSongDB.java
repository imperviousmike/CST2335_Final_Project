package com.example.deezer.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.deezer.members.Song;

import java.util.ArrayList;
import java.util.List;

public class FavSongDB extends SQLiteOpenHelper {

    protected final static String DATABASE_NAME = "favSongDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "FAVSONG";
    public final static String COL_TITLE = "TITLE";
    public final static String COL_DURATION = "DURATION";
    public final static String COL_ALBUMNAME = "ALBUMNAME";
    public final static String COL_ALBUMCOVER = "ALBUMCOVER";
    public final static String COL_ARTIST = "ARTIST";
    public final static String COL_ID = "_id";
    public final String[] columns = {COL_ID, COL_TITLE, COL_DURATION, COL_ALBUMNAME, COL_ALBUMCOVER, COL_ARTIST};

    public FavSongDB(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ("
                + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_TITLE + " TEXT,"
                + COL_DURATION + " TEXT,"
                + COL_ALBUMNAME + " TEXT,"
                + COL_ALBUMCOVER + " TEXT,"
                + COL_ARTIST + " TEXT);");
    }

    public boolean addSong(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_TITLE, song.getTitle());
        values.put(COL_DURATION, song.getDuration());
        values.put(COL_ALBUMNAME, song.getAlbumName());
        values.put(COL_ALBUMCOVER, song.getAlbumCover());
        values.put(COL_ARTIST, song.getArtist());
        return db.insert(TABLE_NAME, null, values) > 0;
    }

    public boolean deleteMessage(Song song) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COL_ID + "=" + song.getId(), null) > 0;
    }

    public List<Song> getAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        List<Song> songList = new ArrayList<>();
        Cursor c = db.query(TABLE_NAME, columns, null, null, null, null, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            printCursor(c, VERSION_NUM);
            songList.add(new Song(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5)));
            c.moveToNext();
        }
        return songList;
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    private void printCursor(Cursor c, int version) {
        Log.i("printCursor", "DB version number: " + version
                + "\nNumber of columns: "
                + c.getColumnCount()
                + "\nColumn Names: " + c.getColumnNames()
                + "\nNumber of rows: " + c.getColumnCount());
    }
}
