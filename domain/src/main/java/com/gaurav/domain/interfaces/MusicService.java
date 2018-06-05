package com.gaurav.domain.interfaces;

import com.gaurav.domain.usecases.CommandUseCases;

import io.reactivex.Observable;

public interface MusicService {

    void play(String path);

    Observable<Integer> observeProgress();

    Observable<Boolean> observeSongCompletion();

    void attachCommandUseCases(CommandUseCases commandUseCases);

    void detachCommandUseCases();
}
