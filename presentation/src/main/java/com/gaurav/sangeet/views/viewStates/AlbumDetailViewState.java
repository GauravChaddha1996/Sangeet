package com.gaurav.sangeet.views.viewStates;

import com.gaurav.domain.models.Album;

public interface AlbumDetailViewState {
    public class Loading implements AlbumDetailViewState {
    }

    public class Error implements AlbumDetailViewState {
    }

    public class Result implements AlbumDetailViewState {
        private Album album;

        public Result(Album album) {
            this.album = album;
        }

        public Album getAlbum() {
            return album;
        }
    }

}
