package com.gaurav.data.models;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import com.gaurav.data.TreeSetIntegerTypeConverter;

import java.util.TreeSet;

@Entity
public class PlaylistEntity {
    @PrimaryKey
    public long id;
    public String name;
    @TypeConverters(TreeSetIntegerTypeConverter.class)
    public TreeSet<Integer> songIds;

    public PlaylistEntity(long id, String name, TreeSet<Integer> songIds) {
        this.id = id;
        this.name = name;
        this.songIds = songIds;
    }
}
