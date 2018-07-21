package com.gaurav.sangeet.views.viewstates;

import com.gaurav.domain.models.Artist;

public interface ArtistDetailViewState {
    public class Loading implements ArtistDetailViewState {
    }

    public class Error implements ArtistDetailViewState {
    }

    public class Result implements ArtistDetailViewState {
        private Artist artist;

        public Result(Artist artist) {
            this.artist = artist;
        }

        public Artist getArtist() {
            return artist;
        }
    }

}
