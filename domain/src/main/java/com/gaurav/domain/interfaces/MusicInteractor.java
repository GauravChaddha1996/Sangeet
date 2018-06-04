package com.gaurav.domain.interfaces;

import com.gaurav.domain.MusicState;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface MusicInteractor {

    Completable init();

    void attachMusicService(MusicService musicService);

    Observable<MusicState> observeMusicState();

    Completable play(Song song);

    Completable play(Album album, long id);

    Completable play(Artist artist, long id);

    Completable play(Playlist playlist, long id);


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
