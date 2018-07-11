package com.gaurav.domain.interfaces;

import com.gaurav.domain.musicState.MusicState;
import com.gaurav.domain.musicState.PartialChanges;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;

import io.reactivex.Completable;
import io.reactivex.subjects.BehaviorSubject;

public interface MusicStateManager {

    Completable init();

    void attachMusicService(MusicService musicService);

    void detachMusicService();

    void attachCommandUseCases(CommandUseCases commandUseCases);

    void detachCommandUseCases();

    MusicState getInitialState();

    BehaviorSubject<MusicState> observeMusicState();

    void transform(PartialChanges changes);
}
