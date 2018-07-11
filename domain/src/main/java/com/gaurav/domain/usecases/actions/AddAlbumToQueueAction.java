package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Album;

public class AddAlbumToQueueAction implements Action {
    private Album album;
    private int pos;

    public AddAlbumToQueueAction(Album album, int pos) {
        this.album = album;
        this.pos = pos;
    }

    public Album getAlbum() {
        return album;
    }

    public int getPos() {
        return pos;
    }
}
