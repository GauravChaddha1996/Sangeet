package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uievents.albumdetails.AlbumDetailUIEvent;
import com.gaurav.sangeet.views.viewstates.AlbumDetailViewState;

import io.reactivex.subjects.PublishSubject;

public interface AlbumDetailView {
    void render(AlbumDetailViewState state);

    PublishSubject<AlbumDetailUIEvent> getUIEvents();
}
