package com.gaurav.domain.interfaces;

import io.reactivex.subjects.PublishSubject;

public interface MusicService {

    void play(String path);

    void pause();

    void resume();

    PublishSubject<Integer> observeProgress();

    PublishSubject<Boolean> observeSongCompletion();
}
