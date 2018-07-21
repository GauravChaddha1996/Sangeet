package com.gaurav.data;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.MediaStore;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
@Singleton
public class MusicRepositoryImpl implements MusicRepository {

    private ContentResolver contentResolver;

    private List<Song> songList;
    private List<Album> albumList;
    private List<Artist> artistList;

    // Helper private fields
    private Album albumResult = null;
    private Artist artistResult = null;

    @Inject
    public MusicRepositoryImpl(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    @Override
    public Completable init() {
        Single<List<Song>> songEntitySingle = Single.fromCallable(this::scanSongs)
                .subscribeOn(Schedulers.io())
                .map(this::processForSongs)
                .map(this::saveSongs)
                .cache();
        Single<List<Album>> albumEntitySingle = songEntitySingle
                .subscribeOn(Schedulers.io())
                .map(this::processForAlbums)
                .map(this::saveAlbums);
        return Single.zip(songEntitySingle, albumEntitySingle, this::processForArtists)
                .subscribeOn(Schedulers.io())
                .map(this::saveArtists)
                .toCompletable();
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return Observable.just(songList);
    }

    @Override
    public Observable<List<Album>> getAllAlbums() {
        return Observable.just(albumList);
    }

    @Override
    public Observable<List<Artist>> getAllArtists() {
        return Observable.just(artistList);
    }

    @Override
    public Single<Album> getAlbum(long albumId) {
        for (Album album : albumList) {
            if (album.id == albumId) {
                albumResult = album;
                break;
            }
        }
        return Single.create(e -> {
            if (albumResult != null) {
                e.onSuccess(albumResult);
            } else {
                e.onError(new Throwable("No such album"));
            }
        });
    }

    @Override
    public Single<Artist> getArtist(long artistId) {
        for (Artist artist : artistList) {
            if (artist.id == artistId) {
                artistResult = artist;
                break;
            }
        }
        return Single.create(e -> {
            if (artistResult != null) {
                e.onSuccess(artistResult);
            } else {
                e.onError(new Throwable("No such artist"));
            }
        });
    }

    /*
     * Helper functions
     * */

    private List<Song> scanSongs() {
        List<Song> songList = new ArrayList<>();
        String[] columns = new String[]{
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.ARTIST_ID,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.YEAR,
                MediaStore.Audio.Media.TRACK,
        };
        Cursor c = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                columns, null, null, null);
        Song song;
        while (c != null && c.moveToNext()) {
            song = new Song();
            song.songId = c.getInt(c.getColumnIndex("_id"));
            song.albumId = c.getInt(c.getColumnIndex("album_id"));
            song.artistId = c.getInt(c.getColumnIndex("artist_id"));
            song.data = c.getString(c.getColumnIndex("_data"));
            song.title = c.getString(c.getColumnIndex("title"));
            song.duration = c.getLong(c.getColumnIndex("duration"));
            song.album = c.getString(c.getColumnIndex("album"));
            song.artist = c.getString(c.getColumnIndex("artist"));
            song.year = c.getInt(c.getColumnIndex("year"));
            song.track = c.getInt(c.getColumnIndex("track"));
            songList.add(song);
        }
        if (c != null) {
            c.close();
        }
        return songList;
    }

    private List<Song> saveSongs(List<Song> songList) {
        this.songList = songList;
        return songList;
    }

    private List<Album> saveAlbums(List<Album> albumList) {
        this.albumList = albumList;
        return albumList;
    }

    private List<Artist> saveArtists(List<Artist> artistList) {
        this.artistList = artistList;
        return artistList;
    }

    private List<Song> processForSongs(List<Song> songList) {
        songList.sort((song1, song2) -> song1.title.compareToIgnoreCase(song2.title));
        return songList;
    }

    private List<Album> processForAlbums(List<Song> songList) {
        Map<String, Album> albumMap = new HashMap<>();
        Album album;
        for (Song song : songList) {
            album = albumMap.getOrDefault(song.album.toLowerCase().trim(),
                    new Album(song.albumId, song.album, song.artistId, song.artist,
                            new TreeSet<>((song1, song2) -> song1.track < song2.track ? -1 : 1)));
            album.songSet.add(song);
            albumMap.put(album.name.toLowerCase().trim(), album);
        }
        List<Album> albumList = new ArrayList<>(albumMap.values());
        albumList.sort((album1, album2) -> album1.name.compareToIgnoreCase(album2.name));
        return albumList;
    }

    private List<Artist> processForArtists(List<Song> songList,
                                           List<Album> albumList) {
        Map<String, Artist> artistMap = new HashMap<>();
        Artist artist;
        for (Song song : songList) {
            artist = artistMap.getOrDefault(song.artist.toLowerCase().trim(),
                    new Artist(song.artistId, song.artist,
                            new TreeSet<>((album1, album2) ->
                                    album1.name.compareToIgnoreCase(album2.name)),
                            new TreeSet<>((song1, song2) ->
                                    song1.title.compareToIgnoreCase(song2.title))));
            artist.songSet.add(song);
            artistMap.put(artist.name.toLowerCase().trim(), artist);
        }
        for (Album album : albumList) {
            artist = artistMap.getOrDefault(album.artistName.toLowerCase().trim(),
                    new Artist(album.artistId, album.artistName,
                            new TreeSet<>((album1, album2) ->
                                    album1.name.compareToIgnoreCase(album2.name)),
                            new TreeSet<>((song1, song2) ->
                                    song1.title.compareToIgnoreCase(song2.title))));
            artist.albumSet.add(album);
            artistMap.put(artist.name.toLowerCase().trim(), artist);
        }
        List<Artist> artistList = new ArrayList<>(artistMap.values());
        artistList.sort((artist1, artist2) -> artist1.name.compareToIgnoreCase(artist2.name));
        return artistList;
    }
}
