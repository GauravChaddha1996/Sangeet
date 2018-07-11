package com.gaurav.domain.interfaces;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicState.MusicState;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Single;

public interface MusicRepository {
    // TODO: 7/5/18 Add all calls for getting single song, album, artists even if you dont call them
    Completable init();

    Observable<List<Song>> getAllSongs();

    Observable<List<Album>> getAllAlbums();

    Observable<List<Artist>> getAllArtists();

    Single<Album> getAlbum(long albumId);

    Single<Artist> getArtist(long artistId);

    MusicState getMusicState();

    Completable saveMusicState(MusicState musicState);
}
