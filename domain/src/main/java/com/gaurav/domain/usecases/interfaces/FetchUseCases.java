package com.gaurav.domain.usecases.interfaces;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public interface FetchUseCases {
    Observable<List<Song>> getAllSongs();

    Observable<List<Album>> getAllAlbums();

    Observable<List<Artist>> getAllArtists();

    Single<Album> getAlbum(long id);

    Single<Artist> getArtist(long id);
}
