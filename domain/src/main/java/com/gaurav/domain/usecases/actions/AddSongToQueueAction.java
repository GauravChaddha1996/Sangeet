package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Song;

public class AddSongToQueueAction implements Action {
    private Song song;
    private int pos;

    public AddSongToQueueAction(Song song, int pos) {
        this.song = song;
        this.pos = pos;
    }

    public Song getSong() {
        return song;
    }

    public int getPos() {
        return pos;
    }


}
