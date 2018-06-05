package com.gaurav.domain.interfaces;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

public interface MusicRepository {

    Completable init();

    Observable<List<Song>> getAllSongs();

    Observable<List<Album>> getAllAlbums();

    Observable<List<Artist>> getAllArtists();

    Observable<List<Playlist>> getAllPlaylists();

    Completable insertPlaylist(Playlist playlist);

    Completable updatePlaylist(Playlist playlist);

    Completable deletePlaylist(long id);

    Maybe<MusicState> getMusicStateOrError();

    Completable saveMusicState(MusicState musicState);
}
