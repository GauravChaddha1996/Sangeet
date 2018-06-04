package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.views.implementations.songs.SongsViewState;

import io.reactivex.Observable;

public interface SongsView {
    void render(SongsViewState songsViewState);

    Observable<Song> playIntent();
}
