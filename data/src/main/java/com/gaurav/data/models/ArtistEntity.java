package com.gaurav.data.models;

import java.util.TreeSet;


public class ArtistEntity {
    public long id;
    public String name;
    public TreeSet<AlbumEntity> albumSet;
    public TreeSet<SongEntity> songSet;

    public ArtistEntity(long id, String name, TreeSet<AlbumEntity> albumSet, TreeSet<SongEntity> songSet) {
        this.id = id;
        this.name = name;
        this.albumSet = albumSet;
        this.songSet = songSet;
    }
}
