package com.gaurav.domain.usecases;

import com.gaurav.domain.PartialChanges;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.actions.Action;

import io.reactivex.subjects.PublishSubject;

public interface CommandUseCases {

    PublishSubject<Action> actionSubject();

    void play(Song song);

    void play(Album album, long id);

    void play(Artist artist, long id);

    void play(Playlist playlist, long id);

    void attachMusicService(MusicService musicService);

    void detachMusicService();

    PublishSubject<PartialChanges> observePartialChanges();

}
