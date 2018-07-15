package com.gaurav.sangeet.views.uiEvents.artistDetails;

import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;

public class PlayArtistDetailUIEvent implements ArtistDetailUIEvent {
    private Artist artist;
    private Song song;

    public PlayArtistDetailUIEvent(Artist artist, Song song) {
        this.artist = artist;
        this.song = song;
    }

    public Artist getArtist() {
        return artist;
    }

    public Song getSong() {
        return song;
    }
}
