package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.views.uievents.songs.SongViewUIEvent;
import com.gaurav.sangeet.views.viewstates.SongsViewState;

import io.reactivex.subjects.PublishSubject;

public interface SongsView {

    void render(SongsViewState songsViewState);

    void currentSongUpdated(Song currentSong);

    PublishSubject<SongViewUIEvent> getUIEvents();
}
