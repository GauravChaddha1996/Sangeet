package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;

import java.util.ArrayList;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;

import static com.gaurav.domain.PartialChanges.CurrentSongIndexChanged;
import static com.gaurav.domain.PartialChanges.QueueUpdated;

public class MusicStateManagerImpl implements MusicStateManager {

    private MusicRepository musicRepository;
    private MusicService musicService;
    private MusicState musicState;
    private BehaviorSubject<MusicState> musicStateBehaviorSubject;

    public MusicStateManagerImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
        this.musicStateBehaviorSubject = BehaviorSubject.createDefault(getInitialState());
    }

    @Override
    public Completable init() {
        this.musicState = getInitialState();
        // todo call repository and feth stored state
        return Completable.complete();
    }

    @Override
    public BehaviorSubject<MusicState> observeMusicState() {
        return musicStateBehaviorSubject;
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
        }
    }

    @Override
    public void transformationsComplete() {
        musicStateBehaviorSubject.onNext(musicState);
    }

    @Override
    public MusicState getInitialState() {
        return new MusicStateBuilder()
                .setShowStatus(false)
                .setProgress(-1)
                .setCurrentSongIndex(0)
                .setShuffle(true)
                .setRepeat(true)
                .setSongQueue(new ArrayList<>())
                .setDisablePrev(false)
                .setPlaying(false)
                .build();
    }
}
