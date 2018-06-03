package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.views.implementations.songs.SongViewState;

import io.reactivex.Observable;

public interface SongView {
    void render(SongViewState songViewState);

    Observable<Song> playIntent();
}
