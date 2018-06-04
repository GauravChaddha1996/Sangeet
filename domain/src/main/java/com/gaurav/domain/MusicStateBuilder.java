package com.gaurav.domain;

import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.List;

public class MusicStateBuilder {
        private boolean showStatus = false;
        private long progress = 0;
        private int currentSongIndex = 0;
        private boolean shuffle = true;
        private boolean repeat = true;
        private List<Song> songQueue = new ArrayList<>();
        private boolean disablePrev = false;
        private boolean isPlaying = false;

        public MusicStateBuilder() {
        }

        public MusicStateBuilder(boolean showStatus, long progress, int currentSongIndex,
                                 boolean shuffle, boolean repeat, List<Song> songQueue, boolean disablePrev, boolean isPlaying) {
            this.showStatus = showStatus;
            this.progress = progress;
            this.currentSongIndex = currentSongIndex;
            this.shuffle = shuffle;
            this.repeat = repeat;
            this.songQueue = songQueue;
            this.disablePrev = disablePrev;
            this.isPlaying = isPlaying;
        }

        public MusicStateBuilder setShowStatus(boolean showStatus) {
            this.showStatus = showStatus;
            return this;
        }

        public MusicStateBuilder  setProgress(long progress) {
            this.progress = progress;
            return this;
        }

        public MusicStateBuilder  setCurrentSongIndex(int currentSongIndex) {
            this.currentSongIndex = currentSongIndex;
            return this;
        }

        public MusicStateBuilder  setShuffle(boolean shuffle) {
            this.shuffle = shuffle;
            return this;
        }

        public MusicStateBuilder  setRepeat(boolean repeat) {
            this.repeat = repeat;
            return this;
        }

        public MusicStateBuilder  setSongQueue(List<Song> songQueue) {
            this.songQueue = songQueue;
            return this;
        }

        public MusicStateBuilder  setDisablePrev(boolean disablePrev) {
            this.disablePrev = disablePrev;
            return this;
        }

        public MusicStateBuilder  setPlaying(boolean playing) {
            isPlaying = playing;
            return this;
        }

        public MusicState build() {
            return new MusicState(showStatus, progress, currentSongIndex, shuffle, repeat, songQueue, disablePrev, isPlaying);
        }
    }
