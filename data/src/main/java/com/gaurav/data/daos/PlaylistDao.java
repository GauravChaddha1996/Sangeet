package com.gaurav.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.gaurav.data.models.PlaylistEntity;

import java.util.List;

@Dao
public interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PlaylistEntity playlist);

    @Query("SELECT * FROM PlaylistEntity")
    List<PlaylistEntity> getAllPlaylists();

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(PlaylistEntity playlistEntity);

    @Query("DELETE FROM PlaylistEntity WHERE id = :id")
    void deletePlaylist(long id);
}
