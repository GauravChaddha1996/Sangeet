package com.gaurav.domain;

import com.gaurav.domain.models.Song;

import java.util.List;

public interface PartialChanges {

    public class QueueUpdated implements PartialChanges {
        private List<Song> songQueue;

        public QueueUpdated(List<Song> songQueue) {
            this.songQueue = songQueue;
        }

        public List<Song> getSongQueue() {
            return songQueue;
        }
    }

    public class CurrentSongIndexChanged implements PartialChanges {
        private int index;

        public CurrentSongIndexChanged(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

}
