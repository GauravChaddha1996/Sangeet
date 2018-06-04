package com.gaurav.sangeet.views.implementations.albums;

import com.gaurav.domain.models.Album;

import java.util.List;

public interface AlbumsViewState {
    class Loading implements AlbumsViewState {
    }

    class Error implements AlbumsViewState {
    }

    class Result implements AlbumsViewState {
        private List<Album> albumList;

        public Result(List<Album> albumList) {
            this.albumList = albumList;
        }

        public List<Album> getAlbumList() {
            return albumList;
        }
    }
}
