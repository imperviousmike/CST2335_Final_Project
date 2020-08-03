package com.example.deezer.members;

import java.io.Serializable;

public class Song implements Serializable {

    private String title;
    private String duration;
    private String albumName;
    private String albumCover;
    private String artist;

    private int id;

    public static final long serialVersionUID = 1L;

    public Song(String title, String duration, String albumName, String albumCover, String artist) {
        this(0,title,duration,albumName,albumCover,artist);
    }

    public Song(int id, String title, String duration, String albumName, String albumCover, String artist) {
        this.id = id;
        this.title = title;
        this.duration = duration;
        this.albumName = albumName;
        this.albumCover = albumCover;
        this.artist = artist;
    }

    public Song() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public String getAlbumCover() {
        return albumCover;
    }

    public void setAlbumCover(String albumCover) {
        this.albumCover = albumCover;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Song)
            return super.equals(obj) || (getTitle().equals(((Song) obj).getTitle()) && (getArtist().equals(((Song) obj).getArtist())));

        return false;
    }

    @Override
    public String toString(){
       return String.format("Title: %s \n Artist: %s", getTitle(), getArtist());
    }


}
