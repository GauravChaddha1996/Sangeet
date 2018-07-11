package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;

public class PlayArtistAction implements Action {
    private Artist artist;
    private Song song;

    public PlayArtistAction(Artist artist, Song song) {
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
