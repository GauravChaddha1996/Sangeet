package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.models.Artist;
import com.gaurav.sangeet.views.implementations.artists.ArtistsViewState;

import io.reactivex.Observable;

public interface ArtistsView {
    void render(ArtistsViewState songsViewState);

    Observable<Artist> playIntent();
}
