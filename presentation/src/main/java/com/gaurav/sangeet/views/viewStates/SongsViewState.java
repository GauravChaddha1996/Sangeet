package com.gaurav.sangeet.views.viewStates;

import com.gaurav.domain.models.Song;

import java.util.List;

public interface SongsViewState {
    public class Loading implements SongsViewState {
    }

    public class Error implements SongsViewState {
    }

    public class Result implements SongsViewState {
        private List<Song> songList;

        public Result(List<Song> songList) {
            this.songList = songList;
        }

        public List<Song> getSongList() {
            return songList;
        }
    }

}
