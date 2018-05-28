package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MusicInteractorImpl implements MusicInteractor {
    private MusicRepository musicRepository;

    public MusicInteractorImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Single<List<Song>> getAllSongs() {
        return musicRepository.getAllSongs();
    }

    @Override
    public Single<List<Album>> getAllAlbums() {
        return musicRepository.getAllAlbums();
    }

    @Override
    public Single<List<Artist>> getAllArtists() {
        return musicRepository.getAllArtists();
    }

    @Override
    public Single<List<Playlist>> getAllPlaylists() {
        return musicRepository.getAllPlaylists();
    }

    @Override
    public Completable insertPlaylist(Playlist playlist) {
        return musicRepository.insertPlaylist(playlist);
    }

    @Override
    public Completable updatePlaylist(Playlist playlist) {
        return musicRepository.updatePlaylist(playlist);
    }

    @Override
    public Completable deletePlaylist(long id) {
        return musicRepository.deletePlaylist(id);
    }
}
