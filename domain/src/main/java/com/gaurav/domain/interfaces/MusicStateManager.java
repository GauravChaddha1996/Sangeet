package com.gaurav.domain.interfaces;

import com.gaurav.domain.musicstate.MusicState;
import com.gaurav.domain.musicstate.PartialChanges;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;

import io.reactivex.Completable;
import io.reactivex.subjects.PublishSubject;

public interface MusicStateManager {

    Completable init();

    void initMusicState();

    MusicState getMusicState();

    void attachMusicService(MusicService musicService);

    void detachMusicService();

    void attachCommandUseCases(CommandUseCases commandUseCases);

    void detachCommandUseCases();

    boolean shuffle();

    PublishSubject<MusicState> observeMusicState();

    void transform(PartialChanges changes);
}
