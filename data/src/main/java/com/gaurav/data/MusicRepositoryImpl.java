package com.gaurav.data;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.MediaStore;

import com.gaurav.data.models.AlbumEntity;
import com.gaurav.data.models.ArtistEntity;
import com.gaurav.data.models.SongEntity;
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
    private List<SongEntity> songList;
    private List<AlbumEntity> albumList;
    private List<ArtistEntity> artistList;


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
        Single<List<SongEntity>> songEntitySingle = Single.fromCallable(this::scanSongs)
                .subscribeOn(Schedulers.io())
                .map(this::processForSongs)
                .map(this::saveSongs)
                .cache();
        Single<List<AlbumEntity>> albumEntitySingle = songEntitySingle
                .subscribeOn(Schedulers.io())
                .map(this::processForAlbums)
                .map(this::saveAlbums);
        return Single.zip(songEntitySingle, albumEntitySingle, this::processForArtists)
                .subscribeOn(Schedulers.io())
                .map(this::saveArtists)
                .toCompletable();
    }

    @Override
    public Single<List<Song>> getAllSongs() {
        return Observable.fromIterable(songList)
                .subscribeOn(Schedulers.io())
                .map(modelMapper::convertSongEntityToSong)
                .toList();
    }

    @Override
    public Single<List<Album>> getAllAlbums() {
        return Observable.fromIterable(albumList)
                .subscribeOn(Schedulers.io())
                .map(modelMapper::convertAlbumEntityToAlbum)
                .toList();
    }

    @Override
    public Single<List<Artist>> getAllArtists() {
        return Observable.fromIterable(artistList)
                .subscribeOn(Schedulers.io())
                .map(modelMapper::convertArtistEntityToArtist)
                .toList();
    }

    @Override
    public Single<List<Playlist>> getAllPlaylists() {
        return Observable.fromCallable(() -> musicDatabase.playlistDao().getAllPlaylists())
                .subscribeOn(Schedulers.io())
                .flatMapIterable(playlistEntities -> playlistEntities)
                .map(modelMapper::convertPlaylistEntityToPlaylist)
                .toList();
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

    private List<SongEntity> saveSongs(List<SongEntity> songEntityList) {
        this.songList = songEntityList;
        return songEntityList;
    }

    private List<AlbumEntity> saveAlbums(List<AlbumEntity> albumEntityList) {
        this.albumList = albumEntityList;
        return albumEntityList;
    }

    private List<ArtistEntity> saveArtists(List<ArtistEntity> artistEntityList) {
        this.artistList = artistEntityList;
        return artistEntityList;
    }

    private List<SongEntity> processForSongs(List<SongEntity> songEntityList) {
        songEntityList.sort((song1, song2) -> song1.title.compareToIgnoreCase(song2.title));
        return songEntityList;
    }

    private List<AlbumEntity> processForAlbums(List<SongEntity> songEntityList) {
        Map<String, AlbumEntity> albumEntityMap = new HashMap<>();
        for (SongEntity songEntity : songEntityList) {
            AlbumEntity albumEntity = albumEntityMap
                    .getOrDefault(songEntity.album.toLowerCase().trim(),
                            new AlbumEntity(songEntity.albumId, songEntity.album,
                                    songEntity.artistId, songEntity.artist,
                                    new TreeSet<>((song1, song2) -> song1.track < song2.track ? -1 : 1)));
            albumEntity.songSet.add(songEntity);
            albumEntityMap.put(albumEntity.name.toLowerCase().trim(), albumEntity);
        }
        List<AlbumEntity> albumEntityList = new ArrayList<>(albumEntityMap.values());
        albumEntityList.sort((album1, album2) -> album1.name.compareToIgnoreCase(album2.name));
        return albumEntityList;
    }

    private List<ArtistEntity> processForArtists(List<SongEntity> songEntityList,
                                                 List<AlbumEntity> albumEntityList) {
        Map<String, ArtistEntity> artistEntityMap = new HashMap<>();
        for (SongEntity songEntity : songEntityList) {
            ArtistEntity artistEntity = artistEntityMap
                    .getOrDefault(songEntity.artist.toLowerCase().trim(),
                            new ArtistEntity(songEntity.artistId, songEntity.artist,
                                    new TreeSet<>((album1, album2) -> album1.name.compareToIgnoreCase(album2.name)),
                                    new TreeSet<>((song1, song2) -> song1.title.compareToIgnoreCase(song2.title))));
            artistEntity.songSet.add(songEntity);
            artistEntityMap.put(artistEntity.name.toLowerCase().trim(), artistEntity);
        }
        for (AlbumEntity albumEntity : albumEntityList) {
            ArtistEntity artistEntity = artistEntityMap
                    .getOrDefault(albumEntity.artistName.toLowerCase().trim(),
                            new ArtistEntity(albumEntity.artistId, albumEntity.artistName,
                                    new TreeSet<>((album1, album2) -> album1.name.compareToIgnoreCase(album2.name)),
                                    new TreeSet<>((song1, song2) -> song1.title.compareToIgnoreCase(song2.title))));
            artistEntity.albumSet.add(albumEntity);
            artistEntityMap.put(artistEntity.name.toLowerCase().trim(), artistEntity);
        }
        List<ArtistEntity> artistEntityList = new ArrayList<>(artistEntityMap.values());
        artistEntityList.sort((artist1, artist2) -> artist1.name.compareToIgnoreCase(artist2.name));
        return artistEntityList;
    }
}
