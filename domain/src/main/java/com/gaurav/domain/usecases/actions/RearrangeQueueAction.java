package com.gaurav.domain.usecases.actions;

import com.gaurav.domain.models.Song;

import java.util.List;

public class RearrangeQueueAction implements Action {
    private List<Song> songQueue;

    public RearrangeQueueAction(List<Song> songQueue) {
        this.songQueue = songQueue;
    }

    public List<Song> getSongQueue() {
        return songQueue;
    }
}
