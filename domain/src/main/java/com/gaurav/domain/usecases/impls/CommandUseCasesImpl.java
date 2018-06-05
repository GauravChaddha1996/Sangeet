package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.PartialChanges;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.CommandUseCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;

public class CommandUseCasesImpl implements CommandUseCases {

    private MusicRepository musicRepository;
    private MusicStateManager musicStateManager;

    private Emitter<PartialChanges> partialChangesEmitter;
    private Observable<PartialChanges> partialChangesObservable;

    private Emitter<Song> songToPlayEmitter;
    private Observable<Song> songToPlayObservable;

    public CommandUseCasesImpl(MusicRepository musicRepository, MusicStateManager musicStateManager) {
        this.musicRepository = musicRepository;
        this.musicStateManager = musicStateManager;
        prepareObservablesAndEmitters();
    }

    @Override
    public void play(Song song) {
        playCommandHelper(song, song);
    }

    @Override
    public void play(Album album, long id) {
        playCommandHelper(album, album.songSet.stream()
                .filter(song -> song.songId == id)
                .findFirst().orElse(album.songSet.first()));
    }

    @Override
    public void play(Artist artist, long id) {
        playCommandHelper(artist, artist.songSet.stream()
                .filter(song -> song.songId == id)
                .findFirst().orElse(artist.songSet.first()));
    }

    @Override
    public void play(Playlist playlist, long id) {
        playCommandHelper(playlist, playlist.songSet.stream()
                .filter(song -> song.songId == id)
                .findFirst().orElse(playlist.songSet.first()));
    }

    @Override
    public Observable<PartialChanges> observePartialChanges() {
        return partialChangesObservable;
    }

    @Override
    public Observable<Song> observeSongToPlay() {
        return songToPlayObservable;
    }

    /*
     * Private helper functions
     * */

    private void prepareObservablesAndEmitters() {
        partialChangesObservable = Observable.create((ObservableEmitter<PartialChanges> emitter) ->
                this.partialChangesEmitter = emitter);
        partialChangesObservable.publish().connect();

        songToPlayObservable = Observable.create((ObservableEmitter<Song> emitter) ->
                this.songToPlayEmitter = emitter);
        songToPlayObservable.publish().connect();
    }

    private void playCommandHelper(Object object, Song song) {
        makeQueue(object)
                .map(this::shuffleQueueIfNeeded)
                .doOnSuccess(songs -> partialChangesEmitter.onNext(new PartialChanges.QueueUpdated(songs)))
                .map(songs -> songs.indexOf(song))
                .doOnSuccess(currentSongIndex -> partialChangesEmitter.onNext(new PartialChanges.CurrentSongIndexChanged(currentSongIndex)))
                .doOnSuccess(__ -> playCurrentSongIndex(song))
                .doOnSuccess(__ -> partialChangesEmitter.onNext(new PartialChanges.PlayingStatusChanged(true)))
                .doOnSuccess(__ -> partialChangesEmitter.onNext(new PartialChanges.Complete()))
                .subscribe().dispose();
    }

    private Single<List<Song>> makeQueue(Object object) {
        if (object instanceof Song) {
            return musicRepository.getAllSongs()
                    .single(new ArrayList<>())
                    .map(ArrayList::new);
        } else if (object instanceof Album) {
            return Single.just(new ArrayList<>(((Album) object).songSet));
        } else if (object instanceof Artist) {
            return Single.just(new ArrayList<>(((Artist) object).songSet));
        } else if (object instanceof Playlist) {
            return Single.just(new ArrayList<>(((Playlist) object).songSet));
        } else {
            return null;
        }
    }

    private List<Song> shuffleQueueIfNeeded(List<Song> songs) {
        if (musicStateManager.observeMusicState().getValue().isShuffle()) {
            Collections.shuffle(songs);
        }
        return songs;
    }

    private void playCurrentSongIndex(Song song) {
        songToPlayEmitter.onNext(song);
    }

}
