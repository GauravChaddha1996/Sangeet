package com.gaurav.domain.usecases.actions;

public class GoToArtistAction implements Action {
    private long artistId;

    public GoToArtistAction(long artistId) {
        this.artistId = artistId;
    }

    public long getArtistId() {
        return artistId;
    }
}
