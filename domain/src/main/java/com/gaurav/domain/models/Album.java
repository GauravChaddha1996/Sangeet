package com.gaurav.domain.models;

import java.util.TreeSet;

public class Album implements Comparable {
    public long id;
    public String name;
    public long artistId;
    public String artistName;
    public TreeSet<Song> songSet;
    public boolean multipleArtists;

    public Album(long id, String name, long artistId, String artistName, TreeSet<Song> songSet) {
        this.id = id;
        this.name = name;
        this.artistId = artistId;
        this.artistName = artistName;
        this.songSet = songSet;
        this.multipleArtists = false;
    }

    @Override
    public int compareTo(Object o) {
        return id < ((Album) o).id ? -1 : 1;
    }
}
