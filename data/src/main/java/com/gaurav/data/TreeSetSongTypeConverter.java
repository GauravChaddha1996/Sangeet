package com.gaurav.data;

import android.arch.persistence.room.TypeConverter;

import com.gaurav.domain.models.Song;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.TreeSet;

public class TreeSetSongTypeConverter {

    @TypeConverter
    public static String toString(TreeSet<Song> songs) {
        return new GsonBuilder().create().toJson(songs);
    }

    @TypeConverter
    public static TreeSet<Song> toTreeSet(String songsString) {
        return new GsonBuilder().create().fromJson(songsString, new TypeToken<TreeSet<Song>>() {
        }.getType());
    }
}
