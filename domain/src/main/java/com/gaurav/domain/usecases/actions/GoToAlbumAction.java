package com.gaurav.domain.usecases.actions;

public class GoToAlbumAction implements Action {
    private long albumId;

    public GoToAlbumAction(long albumId) {
        this.albumId = albumId;
    }

    public long getAlbumId() {
        return albumId;
    }
}
