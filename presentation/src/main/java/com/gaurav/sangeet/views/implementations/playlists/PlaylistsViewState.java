package com.gaurav.sangeet.views.implementations.playlists;

import com.gaurav.domain.models.Playlist;

import java.util.List;

public interface PlaylistsViewState {
    class Loading implements PlaylistsViewState {
    }

    class Error implements PlaylistsViewState {
    }

    class Result implements PlaylistsViewState {
        private List<Playlist> playlistList;

        public Result(List<Playlist> playlistList) {
            this.playlistList = playlistList;
        }

        public List<Playlist> getPlaylistList() {
            return playlistList;
        }
    }
}
