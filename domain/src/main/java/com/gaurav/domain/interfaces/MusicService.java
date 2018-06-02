package com.gaurav.domain.interfaces;

import io.reactivex.Completable;

public interface MusicService {

    void attachMusicInteractor(MusicInteractor musicInteractor);

    Completable play(String path);

}
