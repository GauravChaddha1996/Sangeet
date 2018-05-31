package com.gaurav.domain.interfaces;

import io.reactivex.Completable;

public interface MusicService {
    Completable play(String path);

    void release();
}
