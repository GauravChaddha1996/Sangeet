package com.gaurav.domain;

import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

public class MusicState {
    public boolean showStatus;
    public long progress;
    public int currentSongIndex;
    public boolean shuffle;
    public boolean repeat;
    public List<Song> songQueue;
    public boolean disablePrev;
    public boolean isPlaying;

    public MusicState(boolean showStatus, long progress, int currentSongIndex, boolean shuffle,
                      boolean repeat, List<Song> songQueue, boolean disablePrev, boolean isPlaying) {
        this.showStatus = showStatus;
        this.progress = progress;
        this.currentSongIndex = currentSongIndex;
        this.shuffle = shuffle;
        this.repeat = repeat;
        this.songQueue = songQueue;
        this.disablePrev = disablePrev;
        this.isPlaying = isPlaying;
    }
}
