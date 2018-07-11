package com.gaurav.domain.musicState;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicState.PartialChanges.NextSongRequested;
import com.gaurav.domain.musicState.PartialChanges.PrevSongRequested;
import com.gaurav.domain.musicState.PartialChanges.RepeatToggle;
import com.gaurav.domain.musicState.PartialChanges.ShuffleToggle;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.gaurav.domain.musicState.PartialChanges.CurrentSongIndexChanged;
import static com.gaurav.domain.musicState.PartialChanges.PlayingStatusChanged;
import static com.gaurav.domain.musicState.PartialChanges.ProgressUpdated;
import static com.gaurav.domain.musicState.PartialChanges.QueueUpdated;

public class MusicStateManagerImpl implements MusicStateManager {

    private MusicRepository musicRepository;
    private MusicService musicService;

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
    public BehaviorSubject<MusicState> observeMusicState() {
        return musicStateBehaviorSubject;
    }

    @Override
    public void transform(PartialChanges changes) {
        if (changes instanceof PartialChanges.QueueUpdated) {
            this.musicState = musicState.builder()
                    .setSongQueue(((QueueUpdated) changes).getSongQueue())
                    .build();
        }
        if (changes instanceof PartialChanges.SaveOriginalQueue) {
            this.musicState = musicState.builder()
                    .setOriginalSongQueue(((PartialChanges.SaveOriginalQueue) changes)
                            .getOriginalSongQueue())
                    .build();
        } else if (changes instanceof CurrentSongIndexChanged) {
            this.musicState = musicState.builder()
//                    .setCurrentSongIndex(((CurrentSongIndexChanged) changes).getIndex())
                    .setCurrentSongIndex(musicState.getSongQueue().size() - 1)
                    .build();
        } else if (changes instanceof PlayingStatusChanged) {
            this.musicState = musicState.builder()
                    .setPlaying(((PlayingStatusChanged) changes).isPlaying())
                    .build();
        } else if (changes instanceof PrevSongRequested) {
            if (musicState.getCurrentSongIndex() == 0 && !musicState.isRepeat()) {
                musicService.reset();
                this.musicState = musicState.builder()
                        .setCurrentSongIndex(0)
                        .setProgress(0)
                        .setPlaying(false).build();
            } else {
                int newSongIndex = (musicState.getCurrentSongIndex() - 1 + musicState.getSongQueue().size())
                        % (musicState.getSongQueue().size());
                this.musicState = musicState.builder()
                        .setCurrentSongIndex(newSongIndex)
                        .setProgress(0)
                        .setPlaying(true)
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
                int newSongIndex = (musicState.getCurrentSongIndex() + 1) % (musicState.getSongQueue().size());
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
            List<Song> newQueue = new ArrayList<>(musicState.getOriginalSongQueue());
            if (musicState.isShuffle()) {
                Collections.shuffle(newQueue);
            }
            this.musicState = musicState.builder()
                    .setCurrentSongIndex(newQueue.indexOf(musicState.getCurrentSong()))
                    .setSongQueue(newQueue)
                    .build();
        } else if (changes instanceof RepeatToggle) {
            this.musicState = musicState.builder()
                    .setRepeat(!musicState.isRepeat())
                    .build();
        } else if (changes instanceof PartialChanges.SongCompleted) {
            if ((musicState.getCurrentSongIndex() == musicState.getSongQueue().size() - 1) &&
                    (!musicState.isRepeat())) {
                musicService.reset();
                musicState = musicState.builder()
                        .setCurrentSongIndex(0)
                        .setProgress(0)
                        .setPlaying(false)
                        .build();
            } else {
                int newSongIndex = (musicState.getCurrentSongIndex() + 1) % (musicState.getSongQueue().size());
                musicState = musicState.builder()
                        .setCurrentSongIndex(newSongIndex)
                        .setProgress(0)
                        .build();
                musicService.play(musicState.getCurrentSong().data);
            }
        } else if (changes instanceof PartialChanges.Complete) {
            musicState = musicState.builder().setShowStatus(true).build();
            musicStateBehaviorSubject.onNext(musicState);
        }

        handleMusicStateSave(changes);
    }

    @Override
    public MusicState getInitialState() {
        return new MusicStateBuilder()
                .setShowStatus(false)
                .setProgress(0)
                .setSongQueue(new ArrayList<>())
                .setCurrentSongIndex(0)
                .setShuffle(true)
                .setRepeat(true)
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
