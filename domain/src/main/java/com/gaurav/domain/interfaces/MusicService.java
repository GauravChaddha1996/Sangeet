package com.gaurav.domain.interfaces;

import com.gaurav.domain.usecases.CommandUseCases;

import io.reactivex.Completable;

public interface MusicService {

    void attachCommandUseCases(CommandUseCases commandUseCases);

    Completable play(String path);

}
