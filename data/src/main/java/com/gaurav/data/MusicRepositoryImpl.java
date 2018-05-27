package com.gaurav.data;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

import com.gaurav.data.models.SongEntity;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MusicRepositoryImpl implements MusicRepository {

    private ContentResolver contentResolver;
    private SharedPreferences sharedPreferences;
    private MusicDatabase musicDatabase;
    private ModelMapper modelMapper;
    private List<SongEntity> songList;


    public MusicRepositoryImpl(ContentResolver contentResolver,
                               SharedPreferences sharedPreferences,
                               MusicDatabase musicDatabase) {
        this.contentResolver = contentResolver;
        this.sharedPreferences = sharedPreferences;
        this.musicDatabase = musicDatabase;
        this.modelMapper = new ModelMapper();
    }

    @Override
    public Completable init() {
        return Completable.create(e -> {
            this.songList = scanSongs();
            e.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public Single<List<Song>> getAllSongs() {
        return Observable.fromIterable(songList)
                .subscribeOn(Schedulers.io())
                .map(modelMapper::convertSongEntityToSong)
                .toList();
    }

    /*
     * Helper functions
     * */

    private List<SongEntity> scanSongs() {
        List<SongEntity> songEntityList = new ArrayList<>();
        String[] columns = Constants.getSongColumns();
        Cursor c = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);
        SongEntity songEntity;
        while (c != null && c.moveToNext()) {
            songEntity = new SongEntity();
            songEntity.songId = c.getInt(c.getColumnIndex("_id"));
            songEntity.albumId = c.getInt(c.getColumnIndex("album_id"));
            songEntity.artistId = c.getInt(c.getColumnIndex("artist_id"));
            songEntity.data = c.getString(c.getColumnIndex("_data"));
            songEntity.title = c.getString(c.getColumnIndex("title"));
            songEntity.duration = c.getLong(c.getColumnIndex("duration"));
            songEntity.album = c.getString(c.getColumnIndex("album"));
            songEntity.artist = c.getString(c.getColumnIndex("artist"));
            songEntity.year = c.getInt(c.getColumnIndex("year"));
            songEntity.track = c.getInt(c.getColumnIndex("track"));
            songEntityList.add(songEntity);
        }
        if (c != null) {
            c.close();
        }
        return songEntityList;
    }

}
