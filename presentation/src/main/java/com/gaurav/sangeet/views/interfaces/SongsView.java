package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uiEvents.songs.SongViewUIEvent;
import com.gaurav.sangeet.views.viewStates.SongsViewState;

import io.reactivex.subjects.PublishSubject;

public interface SongsView {

    void render(SongsViewState songsViewState);

    PublishSubject<SongViewUIEvent> getUIEvents();
}
