package com.gaurav.data;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.MusicStateReducer;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MusicRepositoryImpl implements MusicRepository {

    private ContentResolver contentResolver;
    private SharedPreferences sharedPreferences;
    private MusicDatabase musicDatabase;
    private ModelMapper modelMapper;
    private List<Song> songList;
    private List<Album> albumList;
    private List<Artist> artistList;

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
        return Observable.just(songList).cache();
    }

    @Override
    public Observable<List<Album>> getAllAlbums() {
        return Observable.just(albumList).cache();
    }

    @Override
    public Observable<List<Artist>> getAllArtists() {
        return Observable.just(artistList).cache();
    }

    @Override
    public Observable<List<Playlist>> getAllPlaylists() {
        return Observable.fromCallable(() -> musicDatabase.playlistDao().getAllPlaylists())
                .subscribeOn(Schedulers.io())
                .flatMapIterable(playlistEntities -> playlistEntities)
                .map(modelMapper::convertPlaylistEntityToPlaylist)
                .toList()
                .toObservable();

    }

    @Override
    public Completable insertPlaylist(Playlist playlist) {
        return Completable.fromAction(() -> musicDatabase.playlistDao()
                .insert(modelMapper.convertPlaylistToPlaylistEntity(playlist)))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable updatePlaylist(Playlist playlist) {
        return Completable.fromAction(() -> musicDatabase.playlistDao()
                .update(modelMapper.convertPlaylistToPlaylistEntity(playlist)))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Completable deletePlaylist(long id) {
        return Completable.fromAction(() -> musicDatabase.playlistDao()
                .deletePlaylist(id))
                .subscribeOn(Schedulers.io());
    }

    @Override
    public Single<MusicState> getMusicStateOrDefault() {
        // TODO: 5/31/18 Make sure to save music state in database and query it. If not return default
        return Single.just(new MusicStateReducer().emptyState())
                .subscribeOn(Schedulers.io());
    }

    /*
     * Helper functions
     * */

    private List<Song> scanSongs() {
        List<Song> songList = new ArrayList<>();
        String[] columns = Constants.getSongColumns();
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
        for (Song song : songList) {
            Album album = albumMap
                    .getOrDefault(song.album.toLowerCase().trim(),
                            new Album(song.albumId, song.album,
                                    song.artistId, song.artist,
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
        for (Song song : songList) {
            Artist artist = artistMap
                    .getOrDefault(song.artist.toLowerCase().trim(),
                            new Artist(song.artistId, song.artist,
                                    new TreeSet<>((album1, album2) -> album1.name.compareToIgnoreCase(album2.name)),
                                    new TreeSet<>((song1, song2) -> song1.title.compareToIgnoreCase(song2.title))));
            artist.songSet.add(song);
            artistMap.put(artist.name.toLowerCase().trim(), artist);
        }
        for (Album album : albumList) {
            Artist artist = artistMap
                    .getOrDefault(album.artistName.toLowerCase().trim(),
                            new Artist(album.artistId, album.artistName,
                                    new TreeSet<>((album1, album2) -> album1.name.compareToIgnoreCase(album2.name)),
                                    new TreeSet<>((song1, song2) -> song1.title.compareToIgnoreCase(song2.title))));
            artist.albumSet.add(album);
            artistMap.put(artist.name.toLowerCase().trim(), artist);
        }
        List<Artist> artistList = new ArrayList<>(artistMap.values());
        artistList.sort((artist1, artist2) -> artist1.name.compareToIgnoreCase(artist2.name));
        return artistList;
    }
}
