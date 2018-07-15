package com.gaurav.domain.musicState;

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

    public class PlayingStatusChanged implements PartialChanges {
        private boolean isPlaying;

        public PlayingStatusChanged(boolean isPlaying) {
            this.isPlaying = isPlaying;
        }

        public boolean isPlaying() {
            return isPlaying;
        }
    }

    public class PrevSongRequested implements PartialChanges {

    }

    public class NextSongRequested implements PartialChanges {

    }

    public class ShuffleToggle implements PartialChanges {

    }

    public class RepeatToggle implements PartialChanges {

    }

    public class ProgressUpdated implements PartialChanges {
        private int newProgress;

        public ProgressUpdated(int newProgress) {
            this.newProgress = newProgress;
        }

        public int getNewProgress() {
            return newProgress;
        }
    }

    public class SongCompleted implements PartialChanges {

    }

    public class Complete implements PartialChanges {

    }

}
