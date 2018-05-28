package com.gaurav.domain.models;

import java.util.TreeSet;


public class Artist implements Comparable {
    public long id;
    public String name;
    public TreeSet<Album> albumSet;
    public TreeSet<Song> songSet;

    public Artist(long id, String name, TreeSet<Album> albumSet, TreeSet<Song> songSet) {
        this.id = id;
        this.name = name;
        this.albumSet = albumSet;
        this.songSet = songSet;
    }

    @Override
    public int compareTo(Object o) {
        return id < ((Artist) o).id ? -1 : 1;
    }
}

