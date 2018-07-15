package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uiEvents.artistDetails.ArtistDetailUIEvent;
import com.gaurav.sangeet.views.viewStates.ArtistDetailViewState;

import io.reactivex.subjects.PublishSubject;

public interface ArtistDetailView {
    void render(ArtistDetailViewState viewState);

    PublishSubject<ArtistDetailUIEvent> getUIEvents();
}
