package com.gaurav.sangeet.views.viewstates;

import com.gaurav.domain.models.Song;

import java.util.List;

public interface SongsViewState {
    public class Loading implements SongsViewState {
    }

    public class Error implements SongsViewState {
    }

    public class Result implements SongsViewState {
        private List<Song> songList;
        private boolean currentSongIndexChanged;
        private Song currentPlayingSong;

        public Result(List<Song> songList, boolean currentSongIndexChanged, Song currentPlayingSong) {
            this.songList = songList;
            this.currentSongIndexChanged = currentSongIndexChanged;
            this.currentPlayingSong = currentPlayingSong;
        }

        public List<Song> getSongList() {
            return songList;
        }

        public boolean isCurrentSongIndexChanged() {
            return currentSongIndexChanged;
        }

        public Song getCurrentPlayingSong() {
            return currentPlayingSong;
        }
    }

}
