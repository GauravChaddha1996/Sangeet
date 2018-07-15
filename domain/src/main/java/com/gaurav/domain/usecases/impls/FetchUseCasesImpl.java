package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Single;

public class FetchUseCasesImpl implements FetchUseCases {
    MusicRepository musicRepository;

    public FetchUseCasesImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return musicRepository.getAllSongs();
    }

    @Override
    public Observable<List<Album>> getAllAlbums() {
        return musicRepository.getAllAlbums();
    }

    @Override
    public Observable<List<Artist>> getAllArtists() {
        return musicRepository.getAllArtists();
    }

    @Override
    public Single<Album> getAlbum(long id) {
        return musicRepository.getAlbum(id);
    }
}
