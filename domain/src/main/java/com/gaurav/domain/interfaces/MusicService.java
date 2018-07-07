package com.gaurav.domain.interfaces;

import com.gaurav.domain.usecases.CommandUseCases;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public interface MusicService {

    void play(String path);

    PublishSubject<Integer> observeProgress();

    PublishSubject<Boolean> observeSongCompletion();

    void attachCommandUseCases(CommandUseCases commandUseCases);

    void detachCommandUseCases();
}
