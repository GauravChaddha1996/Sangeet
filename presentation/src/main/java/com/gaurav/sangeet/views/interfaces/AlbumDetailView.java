package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uiEvents.albumDetails.AlbumDetailUIEvent;
import com.gaurav.sangeet.views.viewStates.AlbumDetailViewState;

import io.reactivex.subjects.PublishSubject;

public interface AlbumDetailView {
    void render(AlbumDetailViewState state);

    PublishSubject<AlbumDetailUIEvent> getUIEvents();
}
