package com.gaurav.domain.musicstate;

import com.gaurav.domain.models.Song;

import java.util.List;

public class MusicStateBuilder {
    private int currentSongIndex;
    private Song currentSong;
    private boolean isPlaying;
    private long progress;
    private boolean shuffle;
    private boolean repeat;
    private List<Song> songQueue;

    public MusicStateBuilder(int currentSongIndex, Song currentSong, boolean isPlaying,
                             long progress, boolean shuffle, boolean repeat, List<Song> songQueue) {
        this.currentSongIndex = currentSongIndex;
        this.currentSong = currentSong;
        this.isPlaying = isPlaying;
        this.progress = progress;
        this.shuffle = shuffle;
        this.repeat = repeat;
        this.songQueue = songQueue;
    }

    public MusicStateBuilder setProgress(long progress) {
        this.progress = progress;
        return this;
    }

    public MusicStateBuilder setCurrentSongIndex(int currentSongIndex) {
        if (currentSongIndex >= 0 && currentSongIndex < songQueue.size()) {
            this.currentSongIndex = currentSongIndex;
            this.currentSong = songQueue.get(currentSongIndex);
        } else {
            this.currentSongIndex = 0;
            this.currentSong = songQueue.get(0);
        }
        return this;
    }

    public MusicStateBuilder setShuffle(boolean shuffle) {
        this.shuffle = shuffle;
        return this;
    }

    public MusicStateBuilder setRepeat(boolean repeat) {
        this.repeat = repeat;
        return this;
    }

    public MusicStateBuilder setSongQueue(List<Song> songQueue) {
        this.songQueue = songQueue;
        return this;
    }

    public MusicStateBuilder setPlaying(boolean playing) {
        isPlaying = playing;
        return this;
    }

    public MusicState build() {
        return new MusicState(currentSongIndex, currentSong, isPlaying, progress, shuffle,
                repeat, songQueue);
    }
}
