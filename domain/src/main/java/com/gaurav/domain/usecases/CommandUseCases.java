package com.gaurav.domain.usecases;

import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

public interface CommandUseCases {
    void play(Song song);

    void play(Album album);

    void play(Artist artist);

    void play(Playlist playlist);
}
