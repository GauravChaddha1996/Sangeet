package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uiEvents.albums.AlbumViewUIEvent;
import com.gaurav.sangeet.views.viewStates.AlbumsViewState;

import io.reactivex.subjects.PublishSubject;

public interface AlbumsView {
    void render(AlbumsViewState state);

    PublishSubject<AlbumViewUIEvent> getUIEvents();
}
