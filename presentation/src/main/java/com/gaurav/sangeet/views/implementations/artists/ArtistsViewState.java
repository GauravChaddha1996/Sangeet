package com.gaurav.sangeet.views.implementations.artists;

import com.gaurav.domain.models.Artist;

import java.util.List;

public interface ArtistsViewState {
    class Loading implements ArtistsViewState {
    }

    class Error implements ArtistsViewState {
    }

    class Result implements ArtistsViewState {
        private List<Artist> artistList;

        public Result(List<Artist> artistList) {
            this.artistList = artistList;
        }

        public List<Artist> getArtistList() {
            return artistList;
        }
    }
}
