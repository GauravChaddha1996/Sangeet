package com.gaurav.domain.models;

import java.util.TreeSet;

public class Playlist implements Comparable {
    public long id;
    public String name;
    public TreeSet<Integer> songIds;

    public Playlist(long id, String name, TreeSet<Integer> songIds) {
        this.id = id;
        this.name = name;
        this.songIds = songIds;
    }

    @Override
    public int compareTo(Object o) {
        return id < ((Playlist) o).id ? -1 : 1;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songIds=" + songIds +
                '}';
    }
}
