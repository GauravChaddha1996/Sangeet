package com.gaurav.sangeet.views.viewstates;

import com.gaurav.domain.models.Album;

import java.util.List;

public interface AlbumsViewState {
    public class Loading implements AlbumsViewState {
    }

    public class Error implements AlbumsViewState {
    }

    public class Result implements AlbumsViewState {
        private List<Album> albumList;

        public Result(List<Album> albumList) {
            this.albumList = albumList;
        }

        public List<Album> getAlbumList() {
            return albumList;
        }
    }

}
