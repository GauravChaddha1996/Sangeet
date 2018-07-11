package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Artist;

public class PlayArtistAction implements Action {
    private Artist artist;

    public PlayArtistAction(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }
}
