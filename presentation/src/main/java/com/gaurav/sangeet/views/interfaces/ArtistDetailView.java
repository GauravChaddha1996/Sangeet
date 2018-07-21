package com.gaurav.sangeet.views.interfaces;

import com.gaurav.sangeet.views.uievents.artistdetails.ArtistDetailUIEvent;
import com.gaurav.sangeet.views.viewstates.ArtistDetailViewState;

import io.reactivex.subjects.PublishSubject;

public interface ArtistDetailView {
    void render(ArtistDetailViewState viewState);

    PublishSubject<ArtistDetailUIEvent> getUIEvents();
}
