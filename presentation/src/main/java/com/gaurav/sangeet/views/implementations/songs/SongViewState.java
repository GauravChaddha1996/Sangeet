package com.gaurav.sangeet.views.implementations.songs;

import com.gaurav.domain.models.Song;

import java.util.List;

public interface SongViewState {
    public class Loading implements SongViewState {
    }

    public class Error implements SongViewState {
    }

    public class Result implements SongViewState {
        private List<Song> songList;

        public Result(List<Song> songList) {
            this.songList = songList;
        }

        public List<Song> getSongList() {
            return songList;
        }
    }

}
