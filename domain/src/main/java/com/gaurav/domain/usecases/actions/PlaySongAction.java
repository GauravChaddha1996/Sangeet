package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Song;

public class PlaySongAction implements Action {
    Song song;

    public PlaySongAction(Song song) {
        this.song = song;
    }

    public Song getSong() {
        return song;
    }
}
