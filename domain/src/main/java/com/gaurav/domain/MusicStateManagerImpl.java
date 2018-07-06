package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.usecases.CommandUseCases;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.gaurav.domain.PartialChanges.CurrentSongIndexChanged;
import static com.gaurav.domain.PartialChanges.PlayingStatusChanged;
import static com.gaurav.domain.PartialChanges.ProgressUpdated;
import static com.gaurav.domain.PartialChanges.QueueUpdated;

public class MusicStateManagerImpl implements MusicStateManager {

    private MusicRepository musicRepository;
    private MusicService musicService;
    private CommandUseCases commandUseCases;

    private MusicState musicState;
    private BehaviorSubject<MusicState> musicStateBehaviorSubject;
    private CompositeDisposable musicServiceCompositeDisposable;
    private Disposable partialChagesDisposable;

    public MusicStateManagerImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
        this.musicStateBehaviorSubject = BehaviorSubject.createDefault(getInitialState());
        musicServiceCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public Completable init() {
        return Completable.create(emitter -> {
            this.musicState = musicRepository.getMusicState();
            if (musicState == null) {
                musicState = getInitialState();
            }
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void attachMusicService(MusicService musicService) {
        this.musicService = musicService;
        musicServiceCompositeDisposable.add(musicService.observeProgress()
                .subscribe(currentProgress -> {
                    transform(new PartialChanges.ProgressUpdated(currentProgress));
                    transform(new PartialChanges.Complete());
                }));
        musicServiceCompositeDisposable.add(musicService.observeSongCompletion()
                .subscribe(bool -> {
                    transform(new PartialChanges.SongCompleted());
                    transform(new PartialChanges.Complete());
                }));
    }

    @Override
    public void detachMusicService() {
        this.musicService = null;
        if (musicServiceCompositeDisposable != null && !musicServiceCompositeDisposable.isDisposed()) {
            musicServiceCompositeDisposable.dispose();
        }
    }

    @Override
    public void attachCommandUseCases(CommandUseCases commandUseCases) {
        this.commandUseCases = commandUseCases;
        partialChagesDisposable = commandUseCases.observePartialChanges()
                .subscribe(this::transform);
    }

    @Override
    public void detachCommandUseCases() {
        this.commandUseCases = null;
        if (partialChagesDisposable != null && !partialChagesDisposable.isDisposed()) {
            partialChagesDisposable.dispose();
        }
    }

    @Override
    public BehaviorSubject<MusicState> observeMusicState() {
        return musicStateBehaviorSubject;
    }

    @Override
    public void transform(PartialChanges changes) {
        if (changes instanceof PartialChanges.QueueUpdated) {
            this.musicState = musicState.builder()
                    .setSongQueue(((QueueUpdated) changes).getSongQueue())
                    .build();
        } else if (changes instanceof CurrentSongIndexChanged) {
            this.musicState = musicState.builder()
                    .setCurrentSongIndex(((CurrentSongIndexChanged) changes).getIndex())
                    .build();
        } else if (changes instanceof PlayingStatusChanged) {
            this.musicState = musicState.builder()
                    .setPlaying(((PlayingStatusChanged) changes).isPlaying())
                    .build();
        } else if (changes instanceof ProgressUpdated) {
            this.musicState = musicState.builder()
                    .setProgress(((ProgressUpdated) changes).getNewProgress())
                    .build();
        } else if (changes instanceof PartialChanges.SongCompleted) {
            int newSongIndex = 0;
            if (musicState.getSongQueue().size() - 1 == musicState.getCurrentSongIndex()) {
                if (musicState.isRepeat()) {
                    newSongIndex = 0;
                } else {
                    musicState = musicState.builder()
                            .setPlaying(false)
                            .build();
                }
            } else {
                newSongIndex = musicState.getCurrentSongIndex() + 1;
            }
            musicState = musicState.builder()
                    .setCurrentSongIndex(newSongIndex)
                    .setProgress(0)
                    .build();
            if (musicService != null) {
                musicService.play(musicState.getSongQueue().get(musicState.getCurrentSongIndex()).data);
            }
        } else if (changes instanceof PartialChanges.Complete) {
            musicStateBehaviorSubject.onNext(musicState);
        }

        handleMusicStateSave(changes);
    }

    @Override
    public MusicState getInitialState() {
        return new MusicStateBuilder()
                .setShowStatus(false)
                .setProgress(0)
                .setCurrentSongIndex(0)
                .setShuffle(true)
                .setRepeat(true)
                .setSongQueue(new ArrayList<>())
                .setDisablePrev(false)
                .setPlaying(false)
                .build();
    }


    private void handleMusicStateSave(PartialChanges changes) {
        if (changes instanceof PartialChanges.QueueUpdated
                || changes instanceof CurrentSongIndexChanged
                || changes instanceof PlayingStatusChanged
                || changes instanceof PartialChanges.SongCompleted) {
            musicRepository.saveMusicState(musicState).subscribe();
        }
    }
}
