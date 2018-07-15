package com.gaurav.sangeet.views.uiEvents.artists;

import com.gaurav.domain.models.Artist;

public class ArtistItemClickUIEvent implements ArtistsViewUIEvent {
    private Artist artist;

    public ArtistItemClickUIEvent(Artist artist) {
        this.artist = artist;
    }

    public Artist getArtist() {
        return artist;
    }
}
