package com.gaurav.domain.usecases;

import com.gaurav.domain.PartialChanges;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import io.reactivex.Observable;

public interface CommandUseCases {

    void play(Song song);

    void play(Album album, long id);

    void play(Artist artist, long id);

    void play(Playlist playlist, long id);

    Observable<PartialChanges> observePartialChanges();

    Observable<Song> observeSongToPlay();

}
