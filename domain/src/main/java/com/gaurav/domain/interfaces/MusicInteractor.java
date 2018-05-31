package com.gaurav.domain.interfaces;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MusicInteractor {

    Completable init();
    void destroy();

    Observable<MusicState> observeMusicState();

    Single<List<Song>> getAllSongs();
    Single<List<Album>> getAllAlbums();
    Single<List<Artist>> getAllArtists();
    Single<List<Playlist>> getAllPlaylists();

    Completable insertPlaylist(Playlist playlist);
    Completable updatePlaylist(Playlist playlist);
    Completable deletePlaylist(long id);


 /*
    Completable makeQueue(Album album);
    Completable makeQueue(Artist artist);
    Completable makeQueue(Playlist playlist);

    Completable addToQueue(Song song);
    Completable addToQueue(Album album);
    Completable addToQueue(Artist artist);
    Completable addToQueue(Playlist playlist);

    Completable playNext(Song song);
    Completable playNext(Album album);
    Completable playNext(Artist artist);
    Completable playNext(Playlist playlist);

    Completable play(Album album,long id);
    Completable play(Artist artist,long id);
    Completable play(Playlist playlist,long id);

    Completable bringSongIdToTop(long id);

    Completable pause();
    Completable next();
    Completable prev();

*/
    Completable play(Song song);


}
