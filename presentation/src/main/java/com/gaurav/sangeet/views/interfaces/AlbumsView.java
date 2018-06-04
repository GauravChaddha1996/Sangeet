package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.models.Album;
import com.gaurav.sangeet.views.implementations.albums.AlbumsViewState;

import io.reactivex.Observable;

public interface AlbumsView {
    void render(AlbumsViewState state);

    Observable<Album> playIntent();
}
