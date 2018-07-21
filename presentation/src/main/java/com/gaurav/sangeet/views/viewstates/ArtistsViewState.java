package com.gaurav.sangeet.views.viewstates;

import com.gaurav.domain.models.Artist;

import java.util.List;

public interface ArtistsViewState {
    public class Loading implements ArtistsViewState {
    }

    public class Error implements ArtistsViewState {
    }

    public class Result implements ArtistsViewState {
        private List<Artist> artistList;

        public Result(List<Artist> artistList) {
            this.artistList = artistList;
        }

        public List<Artist> getArtistList() {
            return artistList;
        }
    }

}
