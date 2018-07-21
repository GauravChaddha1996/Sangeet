package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uievents.artists.ArtistsViewUIEvent;
import com.gaurav.sangeet.views.viewstates.ArtistsViewState;

import io.reactivex.subjects.PublishSubject;

public interface ArtistsView {
    void render(ArtistsViewState songsViewState);

    PublishSubject<ArtistsViewUIEvent> getUIEvents();
}
