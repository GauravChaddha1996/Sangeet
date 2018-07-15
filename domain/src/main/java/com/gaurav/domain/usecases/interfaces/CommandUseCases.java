package com.gaurav.domain.usecases.interfaces;

import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.musicState.PartialChanges;
import com.gaurav.domain.usecases.actions.Action;

import io.reactivex.subjects.PublishSubject;

public interface CommandUseCases {

    PublishSubject<Action> actionSubject();

    void attachMusicService(MusicService musicService);

    void detachMusicService();

    PublishSubject<PartialChanges> observePartialChanges();

}
