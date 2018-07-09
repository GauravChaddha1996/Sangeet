package com.gaurav.domain;

import com.gaurav.domain.models.Song;

import java.util.List;

public class MusicState {
    private boolean showStatus;
    private long progress;
    private int currentSongIndex;
    private Song currentSong;
    private boolean shuffle;
    private boolean repeat;
    private List<Song> songQueue;
    private boolean disablePrev;
    private boolean isPlaying;

    public MusicState(boolean showStatus, long progress, int currentSongIndex, Song currentSong,
                      boolean shuffle, boolean repeat, List<Song> songQueue, boolean disablePrev,
                      boolean isPlaying) {
        this.showStatus = showStatus;
        this.progress = progress;
        this.currentSongIndex = currentSongIndex;
        this.currentSong = currentSong;
        this.shuffle = shuffle;
        this.repeat = repeat;
        this.songQueue = songQueue;
        this.disablePrev = disablePrev;
        this.isPlaying = isPlaying;
    }

    public boolean isShowStatus() {
        return showStatus;
    }

    public long getProgress() {
        return progress;
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public boolean isShuffle() {
        return shuffle;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public List<Song> getSongQueue() {
        return songQueue;
    }

    public boolean isDisablePrev() {
        return disablePrev;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public MusicStateBuilder builder() {
        return new MusicStateBuilder(showStatus, progress, currentSongIndex, currentSong, shuffle, repeat,
                songQueue, disablePrev, isPlaying);
    }
}
