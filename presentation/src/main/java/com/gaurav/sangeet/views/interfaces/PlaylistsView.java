package com.gaurav.sangeet.views.interfaces;

import com.gaurav.domain.models.Playlist;
import com.gaurav.sangeet.views.implementations.playlists.PlaylistsViewState;

import io.reactivex.Observable;

public interface PlaylistsView {
    void render(PlaylistsViewState playlistsViewState);

    Observable<Playlist> playIntent();
}
