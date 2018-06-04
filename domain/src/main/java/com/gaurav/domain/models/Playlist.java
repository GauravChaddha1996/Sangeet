package com.gaurav.domain.models;

import java.util.Objects;
import java.util.TreeSet;

public class Playlist implements Comparable {
    public long id;
    public String name;
    public TreeSet<Song> songs;

    public Playlist(long id, String name, TreeSet<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }

    @Override
    public int compareTo(Object o) {
        return id < ((Playlist) o).id ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Playlist)) return false;
        Playlist playlist = (Playlist) o;
        return id == playlist.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
