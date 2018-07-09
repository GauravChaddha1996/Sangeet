package com.gaurav.domain.interfaces;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface MusicRepository {
    // TODO: 7/5/18 Add all calls for getting single song, album, artists even if you dont call them
    Completable init();

    Observable<List<Song>> getAllSongs();

    Observable<List<Album>> getAllAlbums();

    Observable<List<Artist>> getAllArtists();

    Observable<List<Playlist>> getAllPlaylists();

    Completable insertPlaylist(Playlist playlist);

    Completable updatePlaylist(Playlist playlist);

    Completable deletePlaylist(long id);

    MusicState getMusicState();

    Completable saveMusicState(MusicState musicState);
}
