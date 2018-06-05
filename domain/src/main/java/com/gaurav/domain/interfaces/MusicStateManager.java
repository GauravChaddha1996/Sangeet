package com.gaurav.domain.interfaces;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.PartialChanges;
import com.gaurav.domain.usecases.CommandUseCases;

import io.reactivex.Completable;
import io.reactivex.subjects.BehaviorSubject;

public interface MusicStateManager {

    Completable init();

    void attachMusicService(MusicService musicService);

    void detachMusicService();

    void attachCommandUseCases(CommandUseCases commandUseCases);

    void detachCommandUseCases();

    MusicState getInitialState();

    BehaviorSubject<MusicState> observeMusicState();

    void transform(PartialChanges changes);

    /*
       Completable makeQueue(Artist artist);
       Completable makeQueue(Playlist playlist);

       Completable addToQueue(Song song);
       Completable addToQueue(Album album);
       Completable addToQueue(Artist artist);
       Completable addToQueue(Playlist playlist);

       Completable playNext(Song song);
       Completable playNext(Album album);
       Completable playNext(Artist artist);
       Completable playNext(Playlist playlist);

       Completable play(Artist artist,long id);
       Completable play(Playlist playlist,long id);

       Completable bringSongIdToTop(long id);

       Completable pause();
       Completable next();
       Completable prev();

   */


}
