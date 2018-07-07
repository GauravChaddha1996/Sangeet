package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.PartialChanges;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.actions.Action;
import com.gaurav.domain.usecases.actions.PlaySongAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CommandUseCasesImpl implements CommandUseCases {

    private MusicRepository musicRepository;
    private MusicStateManager musicStateManager;
    private PublishSubject<Action> actionPublishSubject;
    private PublishSubject<PartialChanges> partialChangesSubject;
    private PublishSubject<Song> songToPlaySubject;

    public CommandUseCasesImpl(MusicRepository musicRepository, MusicStateManager musicStateManager) {
        this.musicRepository = musicRepository;
        this.musicStateManager = musicStateManager;
        prepareObservablesAndEmitters();
    }

    @Override
    public PublishSubject actionSubject() {
        return actionPublishSubject;
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
    public PublishSubject<PartialChanges> observePartialChanges() {
        return partialChangesSubject;
    }

    @Override
    public PublishSubject<Song> observeSongToPlay() {
        return songToPlaySubject;
    }
    /*
     * Private helper functions
     * */

    private void prepareObservablesAndEmitters() {
        actionPublishSubject = PublishSubject.create();
        Disposable disposable = actionPublishSubject.subscribe(action -> {
            // TODO: 7/7/18 think of a better way to do this if-else case
            if (action instanceof PlaySongAction) {
                playCommandHelper(((PlaySongAction) action).getSong(), ((PlaySongAction) action).getSong());
            }
        });
        partialChangesSubject = PublishSubject.create();
        songToPlaySubject = PublishSubject.create();
    }

    private <T> T playCommandHelper(T object, Song song) {
        makeQueue(object)
                .map(this::shuffleQueueIfNeeded)
                .doOnSuccess(songs -> partialChangesSubject.onNext(new PartialChanges.QueueUpdated(songs)))
                .map(songs -> songs.indexOf(song))
                .doOnSuccess(currentSongIndex -> partialChangesSubject.onNext(new PartialChanges.CurrentSongIndexChanged(currentSongIndex)))
                .doOnSuccess(__ -> playCurrentSongIndex(song))
                .doOnSuccess(__ -> partialChangesSubject.onNext(new PartialChanges.PlayingStatusChanged(true)))
                .doOnSuccess(__ -> partialChangesSubject.onNext(new PartialChanges.Complete()))
                .subscribe().dispose();
        return object;
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
        songToPlaySubject.onNext(song);
    }

}
