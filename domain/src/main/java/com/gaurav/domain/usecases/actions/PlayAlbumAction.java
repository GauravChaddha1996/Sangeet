package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Song;

public class PlayAlbumAction implements Action {
    private Album album;
    private Song song;

    public PlayAlbumAction(Album album, Song song) {
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
