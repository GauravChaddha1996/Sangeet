package com.gaurav.data.models;

import java.util.TreeSet;

public class AlbumEntity {
    public long id;
    public String name;
    public long artistId;
    public String artistName;
    public TreeSet<SongEntity> songSet;

    public AlbumEntity(long id, String name, long artistId, String artistName,
                       TreeSet<SongEntity> songSet) {
        this.id = id;
        this.name = name;
        this.artistId = artistId;
        this.artistName = artistName;
        this.songSet = songSet;
    }
}
