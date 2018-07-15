package com.gaurav.sangeet.views.uiEvents.albums;

import com.gaurav.domain.models.Album;

public class AlbumItemClickUIEvent implements AlbumViewUIEvent {
    private Album album;

    public AlbumItemClickUIEvent(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }
}
