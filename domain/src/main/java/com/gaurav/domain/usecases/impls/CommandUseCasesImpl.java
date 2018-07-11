package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicState.PartialChanges;
import com.gaurav.domain.usecases.actions.Action;
import com.gaurav.domain.usecases.actions.NextSongAction;
import com.gaurav.domain.usecases.actions.PauseSongAction;
import com.gaurav.domain.usecases.actions.PlaySongAction;
import com.gaurav.domain.usecases.actions.PrevSongAction;
import com.gaurav.domain.usecases.actions.RepeatAction;
import com.gaurav.domain.usecases.actions.ResumeSongAction;
import com.gaurav.domain.usecases.actions.ShuffleAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

public class CommandUseCasesImpl implements CommandUseCases {

    private MusicRepository musicRepository;
    private MusicStateManager musicStateManager;
    private MusicService musicService;

    private PublishSubject<Action> actionPublishSubject;
    private PublishSubject<PartialChanges> partialChangesSubject;

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
    public void attachMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void detachMusicService() {
        this.musicService = null;
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
    public PublishSubject<PartialChanges> observePartialChanges() {
        return partialChangesSubject;
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
            } else if (action instanceof PauseSongAction) {
                musicService.pause();
                partialChangesSubject.onNext(new PartialChanges.PlayingStatusChanged(false));
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof ResumeSongAction) {
                musicService.resume();
                partialChangesSubject.onNext(new PartialChanges.PlayingStatusChanged(true));
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof PrevSongAction) {
                System.out.println("Action is Prev");
            } else if (action instanceof NextSongAction) {
                System.out.println("Action is Next");
            } else if (action instanceof ShuffleAction) {
                System.out.println("Action is Shuffle");
            } else if (action instanceof RepeatAction) {
                System.out.println("Action is Repeat");
            }
        });
        partialChangesSubject = PublishSubject.create();
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
        musicService.play(song.data);
    }

}
