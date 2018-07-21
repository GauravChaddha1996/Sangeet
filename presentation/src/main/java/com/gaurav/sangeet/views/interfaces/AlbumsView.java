package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uievents.albums.AlbumViewUIEvent;
import com.gaurav.sangeet.views.viewstates.AlbumsViewState;

import io.reactivex.subjects.PublishSubject;

public interface AlbumsView {
    void render(AlbumsViewState state);

    PublishSubject<AlbumViewUIEvent> getUIEvents();
}
