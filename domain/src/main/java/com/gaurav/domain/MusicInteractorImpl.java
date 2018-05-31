package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

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

    public MusicInteractorImpl(MusicRepository musicRepository, MusicService musicService) {
        this.musicRepository = musicRepository;
        this.musicService = musicService;
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
    public Observable<MusicState> observeMusicState() {
        return Observable.create(emitter -> musicStateEmitter = emitter);
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

    @Override
    public Completable play(Song song) {
        // make all song queue
        // shuffle if needed
        // find song index and change current song index to it
        // play the song
        return makeQueue()
                .map(this::shuffleQueueIfNeeded)
                .map(musicState1 -> markCurrentSongIndex(musicState1, song))
                .map(this::playCurrentSongIndex)
                .map(this::updateMusicState)
                .ignoreElement();
    }

    private MusicState updateMusicState(MusicState musicState) {
        this.musicState = musicState;
        if(musicStateEmitter!=null) musicStateEmitter.onNext(this.musicState);
        return this.musicState;
    }

    private Single<MusicState> makeQueue() {
        return musicRepository.getAllSongs()
                .map(songs -> musicStateReducer.allSongState(musicState, songs));
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


    @Override
    public void destroy() {
        musicService.release();
    }
}
