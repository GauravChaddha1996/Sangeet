package com.gaurav.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import com.gaurav.data.daos.PlaylistDao;
import com.gaurav.data.models.PlaylistEntity;

@Database(entities = {PlaylistEntity.class}, version = 1, exportSchema = false)
@TypeConverters(TreeSetIntegerTypeConverter.class)
public abstract class MusicDatabase extends RoomDatabase {
    public abstract PlaylistDao playlistDao();
}
