package com.gaurav.sangeet.views.viewstates;

import com.gaurav.domain.models.Song;
import com.gaurav.domain.musicstate.MusicState;

public class BottomSheetViewState {
    private MusicState musicState;
    private boolean updateCurrentSongDetails;
    private Song currentSong;

    public BottomSheetViewState(MusicState musicState, boolean updateCurrentSongDetails,
                                Song currentSong) {
        this.musicState = musicState;
        this.updateCurrentSongDetails = updateCurrentSongDetails;
        this.currentSong = currentSong;
    }

    public MusicState getMusicState() {
        return musicState;
    }

    public boolean isUpdateCurrentSongDetails() {
        return updateCurrentSongDetails;
    }

    public Song getCurrentSong() {
        return currentSong;
    }
}
