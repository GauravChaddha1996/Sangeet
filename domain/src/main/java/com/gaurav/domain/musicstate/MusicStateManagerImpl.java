package com.gaurav.domain.musicstate;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.musicstate.PartialChanges.Complete;
import com.gaurav.domain.musicstate.PartialChanges.CurrentSongIndexChanged;
import com.gaurav.domain.musicstate.PartialChanges.NextSongRequested;
import com.gaurav.domain.musicstate.PartialChanges.PlayingStatusChanged;
import com.gaurav.domain.musicstate.PartialChanges.PrevSongRequested;
import com.gaurav.domain.musicstate.PartialChanges.ProgressUpdated;
import com.gaurav.domain.musicstate.PartialChanges.QueueUpdated;
import com.gaurav.domain.musicstate.PartialChanges.RepeatToggle;
import com.gaurav.domain.musicstate.PartialChanges.ShuffleToggle;
import com.gaurav.domain.musicstate.PartialChanges.SongCompleted;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

@Singleton
public class MusicStateManagerImpl implements MusicStateManager {

    private MusicRepository musicRepository;
    private MusicService musicService;

    private MusicState musicState;
    private PublishSubject<MusicState> musicStateSubject;
    private CompositeDisposable musicServiceCompositeDisposable;
    private Disposable partialChagesDisposable;

    @Inject
    public MusicStateManagerImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
        this.musicStateSubject = PublishSubject.create();
        musicServiceCompositeDisposable = new CompositeDisposable();
    }

    @Override
    public Completable init() {
        return Completable.create(emitter -> {
            this.musicState = null;
            emitter.onComplete();
        }).subscribeOn(Schedulers.io());
    }

    @Override
    public void attachMusicService(MusicService musicService) {
        this.musicService = musicService;
        musicServiceCompositeDisposable.add(musicService.observeProgress()
                .subscribe(currentProgress -> {
                    transform(new ProgressUpdated(currentProgress));
                    transform(new Complete());
                }));
        musicServiceCompositeDisposable.add(musicService.observeSongCompletion()
                .subscribe(bool -> {
                    transform(new SongCompleted());
                    transform(new Complete());
                }));
    }

    @Override
    public void detachMusicService() {
        this.musicService = null;
        if (musicServiceCompositeDisposable != null &&
                !musicServiceCompositeDisposable.isDisposed()) {
            musicServiceCompositeDisposable.dispose();
        }
    }

    @Override
    public void attachCommandUseCases(CommandUseCases commandUseCases) {
        partialChagesDisposable = commandUseCases.observePartialChanges()
                .subscribe(this::transform);
    }

    @Override
    public void detachCommandUseCases() {
        if (partialChagesDisposable != null && !partialChagesDisposable.isDisposed()) {
            partialChagesDisposable.dispose();
        }
    }

    @Override
    public boolean shuffle() {
        if (musicState != null) {
            return musicState.isShuffle();
        }
        return true;
    }

    @Override
    public void initMusicState() {
        if (musicState == null) {
            musicState = new MusicState(0, null,
                    false, 0, false, true, new ArrayList<>());
        }
    }

    @Override
    public MusicState getMusicState() {
        return musicState;
    }

    @Override
    public PublishSubject<MusicState> observeMusicState() {
        return musicStateSubject;
    }

    @Override
    public void transform(PartialChanges changes) {
        if (changes instanceof QueueUpdated) {
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
        } else if (changes instanceof PrevSongRequested) {
            if (musicState.getCurrentSongIndex() == 0) {
                musicService.reset();
                this.musicState = musicState.builder()
                        .setCurrentSongIndex(0)
                        .setProgress(0)
                        .setPlaying(false).build();
            } else {
                this.musicState = musicState.builder()
                        .setCurrentSongIndex(musicState.getCurrentSongIndex() - 1)
                        .setPlaying(true)
                        .setProgress(0)
                        .build();
                musicService.play(musicState.getCurrentSong().data);
            }
        } else if (changes instanceof NextSongRequested) {
            if ((musicState.getCurrentSongIndex() == musicState.getSongQueue().size() - 1) &&
                    (!musicState.isRepeat())) {
                musicService.reset();
                musicState = musicState.builder()
                        .setCurrentSongIndex(0)
                        .setProgress(0)
                        .setPlaying(false)
                        .build();
            } else {
                int newSongIndex = (musicState.getCurrentSongIndex() + 1)
                        % (musicState.getSongQueue().size());
                musicState = musicState.builder()
                        .setCurrentSongIndex(newSongIndex)
                        .setProgress(0)
                        .setPlaying(true)
                        .build();
                musicService.play(musicState.getCurrentSong().data);
            }
        } else if (changes instanceof ProgressUpdated) {
            this.musicState = musicState.builder()
                    .setProgress(((ProgressUpdated) changes).getNewProgress())
                    .build();
        } else if (changes instanceof ShuffleToggle) {
            this.musicState = musicState.builder()
                    .setShuffle(!musicState.isShuffle())
                    .build();
            if (musicState.isShuffle()) {
                Collections.shuffle(this.musicState.getSongQueue());
            }
            this.musicState = musicState.builder()
                    .setCurrentSongIndex(musicState.getSongQueue()
                            .indexOf(musicState.getCurrentSong()))
                    .setSongQueue(musicState.getSongQueue())
                    .build();
        } else if (changes instanceof RepeatToggle) {
            this.musicState = musicState.builder()
                    .setRepeat(!musicState.isRepeat())
                    .build();
        } else if (changes instanceof SongCompleted) {
            if ((musicState.getCurrentSongIndex() == musicState.getSongQueue().size() - 1) &&
                    (!musicState.isRepeat())) {
                musicService.reset();
                musicState = musicState.builder()
                        .setCurrentSongIndex(0)
                        .setProgress(0)
                        .setPlaying(false)
                        .build();
            } else {
                int newSongIndex = (musicState.getCurrentSongIndex() + 1) %
                        (musicState.getSongQueue().size());
                musicState = musicState.builder()
                        .setCurrentSongIndex(newSongIndex)
                        .setProgress(0)
                        .build();
                musicService.play(musicState.getCurrentSong().data);
            }
        } else if (changes instanceof Complete && musicState!=null) {
                musicStateSubject.onNext(musicState);
        }
    }
}
