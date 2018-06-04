package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
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
    public Completable play(Song song) {
        return makeQueue()
                .map(this::shuffleQueueIfNeeded)
                .map(musicState1 -> markCurrentSongIndex(musicState1, song))
                .map(this::playCurrentSongIndex)
                .map(this::updateMusicState)
                .ignoreElements();
    }

    @Override
    public Completable play(Album album, long id) {
        return makeQueue(album)
                .map(this::shuffleQueueIfNeeded)
                .map(musicState1 -> markCurrentSongIndex(musicState1, album.songSet.stream()
                        .filter(song1 -> song1.songId == id).findFirst().orElse(null)))
                .map(this::playCurrentSongIndex)
                .map(this::updateMusicState)
                .ignoreElements();
    }

    @Override
    public Completable play(Artist artist, long id) {
        return makeQueue(artist)
                .map(this::shuffleQueueIfNeeded)
                .map(musicState1 -> markCurrentSongIndex(musicState1, artist.songSet.stream()
                        .filter(song1 -> song1.songId == id).findFirst().orElse(null)))
                .map(this::playCurrentSongIndex)
                .map(this::updateMusicState)
                .ignoreElements();
    }

    @Override
    public Completable play(Playlist playlist, long id) {
        return makeQueue(playlist)
                .map(this::shuffleQueueIfNeeded)
                .map(musicState1 -> markCurrentSongIndex(musicState1, playlist.songs.stream()
                        .filter(song1 -> song1.songId == id).findFirst().orElse(null)))
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
                .map(songs -> musicStateReducer.initialPartialState(musicState, songs));
    }

    private Observable<MusicState> makeQueue(Album album) {
        return Observable.just(musicStateReducer.initialPartialState(musicState, album.songSet));
    }

    private Observable<MusicState> makeQueue(Artist artist) {
        return Observable.just(musicStateReducer.initialPartialState(musicState, artist.songSet));
    }

    private Observable<MusicState> makeQueue(Playlist playlist) {
        return Observable.just(musicStateReducer.initialPartialState(musicState, playlist.songs));
    }

    private MusicState shuffleQueueIfNeeded(MusicState musicState) {
        if (musicState.shuffle) {
            return musicStateReducer.shuffleQueue(musicState);
        }
        return musicState;
    }

    private MusicState markCurrentSongIndex(MusicState musicState, Song song) {
        return musicStateReducer.markCurrentSongIndex(musicState, song != null ? musicState.songQueue.indexOf(song) : 0);
    }

    private MusicState playCurrentSongIndex(MusicState musicState) {
        musicService.play(musicState.songQueue.get(musicState.currentSongIndex).data).subscribe();
        return musicStateReducer.playCurrentSongIndex(musicState);
    }

}
