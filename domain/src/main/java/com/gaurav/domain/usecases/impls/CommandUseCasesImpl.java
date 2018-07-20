package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicState.PartialChanges;
import com.gaurav.domain.usecases.actions.Action;
import com.gaurav.domain.usecases.actions.GoToAlbumAction;
import com.gaurav.domain.usecases.actions.GoToArtistAction;
import com.gaurav.domain.usecases.actions.NextSongAction;
import com.gaurav.domain.usecases.actions.PauseSongAction;
import com.gaurav.domain.usecases.actions.PlayAlbumAction;
import com.gaurav.domain.usecases.actions.PlayArtistAction;
import com.gaurav.domain.usecases.actions.PlaySongAction;
import com.gaurav.domain.usecases.actions.PrevSongAction;
import com.gaurav.domain.usecases.actions.RepeatAction;
import com.gaurav.domain.usecases.actions.ResumeSongAction;
import com.gaurav.domain.usecases.actions.SeekbarMovementAction;
import com.gaurav.domain.usecases.actions.ShuffleAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class CommandUseCasesImpl implements CommandUseCases {

    private MusicRepository musicRepository;
    private MusicStateManager musicStateManager;
    private MusicService musicService;

    private PublishSubject<Action> actionPublishSubject;
    private PublishSubject<PartialChanges> partialChangesSubject;

    @Inject
    public CommandUseCasesImpl(MusicRepository musicRepository, MusicStateManager musicStateManager) {
        this.musicRepository = musicRepository;
        this.musicStateManager = musicStateManager;
        prepareObservablesAndEmitters();
    }

    @Override
    public PublishSubject<Action> actionSubject() {
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
    public PublishSubject<PartialChanges> observePartialChanges() {
        return partialChangesSubject;
    }

    /*
     * Private helper functions
     * */

    private void prepareObservablesAndEmitters() {
        actionPublishSubject = PublishSubject.create();
        Disposable disposable = actionPublishSubject.subscribe(action -> {
            musicStateManager.initMusicState();
            // TODO: 7/7/18 think of a better way to do this if-else case
            if (action instanceof PlaySongAction) {
                playCommandHelper(((PlaySongAction) action).getSong(),
                        ((PlaySongAction) action).getSong());
            } else if (action instanceof PlayAlbumAction) {
                playCommandHelper(((PlayAlbumAction) action).getAlbum(),
                        ((PlayAlbumAction) action).getSong());
            } else if (action instanceof PlayArtistAction) {
                playCommandHelper(((PlayArtistAction) action).getArtist(),
                        ((PlayArtistAction) action).getSong());
            } else if (action instanceof PauseSongAction) {
                musicService.pause();
                partialChangesSubject.onNext(new PartialChanges.PlayingStatusChanged(false));
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof ResumeSongAction) {
                if (musicService.isMediaPlayerSet()) {
                    musicService.resume();
                    partialChangesSubject.onNext(new PartialChanges.PlayingStatusChanged(true));
                    partialChangesSubject.onNext(new PartialChanges.Complete());
                } else {
                    partialChangesSubject.onNext(new PartialChanges.PlayingStatusChanged(true));
                    partialChangesSubject.onNext(new PartialChanges.Complete());
                    musicService.play(musicStateManager.getMusicState().getCurrentSong().data);
                }
            } else if (action instanceof PrevSongAction) {
                partialChangesSubject.onNext(new PartialChanges.PrevSongRequested());
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof NextSongAction) {
                partialChangesSubject.onNext(new PartialChanges.NextSongRequested());
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof ShuffleAction) {
                partialChangesSubject.onNext(new PartialChanges.ShuffleToggle());
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof RepeatAction) {
                partialChangesSubject.onNext(new PartialChanges.RepeatToggle());
                partialChangesSubject.onNext(new PartialChanges.Complete());
            } else if (action instanceof SeekbarMovementAction) {
                System.out.println("Action is SeekbarMovement");
            } else if (action instanceof GoToAlbumAction) {
                System.out.println("Action is GoToAlbum");
            } else if (action instanceof GoToArtistAction) {
                System.out.println("Action is GoToArtist");
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
        if (musicStateManager.shuffle()) {
            Collections.shuffle(songs);
        }
        return songs;
    }

    private void playCurrentSongIndex(Song song) {
        musicService.play(song.data);
    }

}
