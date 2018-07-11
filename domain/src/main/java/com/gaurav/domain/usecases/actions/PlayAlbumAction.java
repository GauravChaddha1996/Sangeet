package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Album;

public class PlayAlbumAction implements Action {
    private Album album;

    public PlayAlbumAction(Album album) {
        this.album = album;
    }

    public Album getAlbum() {
        return album;
    }
}
