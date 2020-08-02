package com.example.deezer.members;

public class Artist {
    private String name;
    private String picture;
    private String songs;

    public Artist(String name, String picture, String songs) {
        this.name = name;
        this.picture = picture;
        this.songs = songs;
    }

    public Artist(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getSongs() {
        return songs;
    }

    public void setSongs(String songs) {
        this.songs = songs;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Artist)
            return super.equals(obj) || getName().equals(((Artist) obj).getName());

        return false;
    }

    @Override
    public String toString(){
        return getName();
    }

}
