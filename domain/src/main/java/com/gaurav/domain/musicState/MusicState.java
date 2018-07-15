package com.gaurav.domain.musicState;

import com.gaurav.domain.models.Song;

import java.util.List;

public class MusicState {
    private int currentSongIndex;
    private Song currentSong;
    private boolean isPlaying;
    private long progress;
    private boolean shuffle;
    private boolean repeat;
    private List<Song> songQueue;

    public MusicState(int currentSongIndex, Song currentSong, boolean isPlaying, long progress,
                      boolean shuffle, boolean repeat, List<Song> songQueue) {
        this.currentSongIndex = currentSongIndex;
        this.currentSong = currentSong;
        this.isPlaying = isPlaying;
        this.progress = progress;
        this.shuffle = shuffle;
        this.repeat = repeat;
        this.songQueue = songQueue;
    }

    public MusicStateBuilder builder() {
        return new MusicStateBuilder(currentSongIndex, currentSong, isPlaying, progress, shuffle,
                repeat, songQueue);
    }

    public int getCurrentSongIndex() {
        return currentSongIndex;
    }

    public Song getCurrentSong() {
        return currentSong;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public long getProgress() {
        return progress;
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
}
