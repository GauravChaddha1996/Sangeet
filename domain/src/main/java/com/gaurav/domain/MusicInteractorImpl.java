package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MusicInteractorImpl implements MusicInteractor {

    private MusicRepository musicRepository;
    private MusicService musicService;
    private MusicState musicState;
    private MusicStateReducer musicStateReducer;
    private ObservableEmitter<MusicState> musicStateEmitter;

    public MusicInteractorImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
        musicStateReducer = new MusicStateReducer();
    }

    @Override
    public Completable init() {
        return musicRepository.getMusicStateOrDefault()
                .subscribeOn(Schedulers.io())
                .map(musicState1 -> musicState = musicState1)
                .ignoreElement();
    }


    @Override
    public void attachMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public Observable<MusicState> observeMusicState() {
        return Observable.create(emitter -> musicStateEmitter = emitter);
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

    @Override
    public Completable play(Song song) {
        return makeQueue()
                .map(this::shuffleQueueIfNeeded)
                .map(musicState1 -> markCurrentSongIndex(musicState1, song))
                .map(this::playCurrentSongIndex)
                .map(this::updateMusicState)
                .ignoreElements();
    }

    private MusicState updateMusicState(MusicState musicState) {
        this.musicState = musicState;
        if (musicStateEmitter != null) musicStateEmitter.onNext(this.musicState);
        return this.musicState;
    }

    private Observable<MusicState> makeQueue() {
        return musicRepository.getAllSongs()
                .map(songs -> musicStateReducer.allSongState(musicState, new ArrayList<>(songs)));
    }

    private MusicState shuffleQueueIfNeeded(MusicState musicState) {
        if (musicState.shuffle) {
            return musicStateReducer.shuffleQueue(musicState);
        }
        return musicState;
    }

    private MusicState markCurrentSongIndex(MusicState musicState, Song song) {
        return musicStateReducer.markCurrentSongIndex(musicState, musicState.songQueue.indexOf(song));
    }

    private MusicState playCurrentSongIndex(MusicState musicState) {
        musicService.play(musicState.songQueue.get(musicState.currentSongIndex).data).subscribe();
        return musicStateReducer.playCurrentSongIndex(musicState);
    }

}
