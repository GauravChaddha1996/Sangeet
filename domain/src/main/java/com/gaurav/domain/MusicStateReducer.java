package com.gaurav.domain;

import com.gaurav.domain.models.Song;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sun.nio.cs.ext.MS874;

public class MusicStateReducer {

    public MusicState emptyState() {
        return new MusicState(
                false,
                -1,
                -1,
                true,
                true,
                new ArrayList<Song>(),
                false,
                false);
    }

    public MusicState allSongState(MusicState musicState, List<Song> songList) {
        return new MusicState(
                false,
                -1,
                -1,
                musicState.shuffle,
                musicState.repeat,
                songList,
                false,
                false);
    }

    public MusicState shuffleQueue(MusicState musicState) {
        Collections.shuffle(musicState.songQueue);
        return new MusicState(
                false,
                -1,
                -1,
                musicState.shuffle,
                musicState.repeat,
                musicState.songQueue,
                false,
                false);
    }

    public MusicState markCurrentSongIndex(MusicState musicState, int currentSongIndex) {
        return new MusicState(
                false,
                -1,
                currentSongIndex,
                musicState.shuffle,
                musicState.repeat,
                musicState.songQueue,
                false,
                false);
    }

    public MusicState playCurrentSongIndex(MusicState musicState) {
        return new MusicState(
                true,
                0,
                musicState.currentSongIndex,
                musicState.shuffle,
                musicState.repeat,
                musicState.songQueue,
                false,
                true);
    }


}
