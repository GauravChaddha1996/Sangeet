package com.gaurav.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.gaurav.data.TreeSetSongTypeConverter;
import com.gaurav.domain.models.Song;

import java.util.TreeSet;

@Entity
public class PlaylistEntity {
    @PrimaryKey
    public long id;
    public String name;
    @TypeConverters(TreeSetSongTypeConverter.class)
    public TreeSet<Song> songs;

    public PlaylistEntity(long id, String name, TreeSet<Song> songs) {
        this.id = id;
        this.name = name;
        this.songs = songs;
    }
}
