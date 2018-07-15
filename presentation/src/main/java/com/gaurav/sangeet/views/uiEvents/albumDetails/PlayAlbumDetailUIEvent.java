package com.gaurav.sangeet.views.uiEvents.albumDetails;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Song;

public class PlayAlbumDetailUIEvent implements AlbumDetailUIEvent {
    private Album album;
    private Song song;

    public PlayAlbumDetailUIEvent(Album album, Song song) {
        this.album = album;
        this.song = song;
    }

    public Album getAlbum() {
        return album;
    }

    public Song getSong() {
        return song;
    }
}
