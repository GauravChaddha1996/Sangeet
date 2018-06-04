package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.PartialChanges;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.interfaces.MusicService;
import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.CommandUseCases;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;

public class CommandUseCasesImpl implements CommandUseCases {

    MusicRepository musicRepository;
    MusicStateManager musicStateManager;
    MusicService musicService;

    public CommandUseCasesImpl(MusicRepository musicRepository, MusicStateManager musicStateManager) {
        this.musicRepository = musicRepository;
        this.musicStateManager = musicStateManager;
    }

    @Override
    public void attachMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void play(Song song) {
        playCommandHelper(song, song);
    }

    @Override
    public void play(Album album, long id) {
        playCommandHelper(album, album.songSet.stream()
                .filter(song -> song.songId == id)
                .findFirst().orElse(album.songSet.first()));
    }

    @Override
    public void play(Artist artist, long id) {
        playCommandHelper(artist, artist.songSet.stream()
                .filter(song -> song.songId == id)
                .findFirst().orElse(artist.songSet.first()));
    }

    @Override
    public void play(Playlist playlist, long id) {
        playCommandHelper(playlist, playlist.songSet.stream()
                .filter(song -> song.songId == id)
                .findFirst().orElse(playlist.songSet.first()));
    }


    /*
     * Private helper functions
     * */

    private void playCommandHelper(Object object, Song song) {
        makeQueue(object)
                .map(this::shuffleQueueIfNeeded)
                .doOnNext(songs -> musicStateManager.transform(new PartialChanges.QueueUpdated(songs)))
                .map(songs -> songs.indexOf(song))
                .doOnNext(currentSongIndex -> musicStateManager.transform(new PartialChanges.CurrentSongIndexChanged(currentSongIndex)))
                .doOnNext(__ -> playCurrentSongIndex(song))
                .doOnNext(__ -> musicStateManager.transformationsComplete())
                .subscribe();
    }

    private Observable<List<Song>> makeQueue(Object object) {
        if (object instanceof Song) {
            return musicRepository.getAllSongs();
        } else if (object instanceof Album) {
            return Observable.just(new ArrayList<>(((Album) object).songSet));
        } else if (object instanceof Artist) {
            return Observable.just(new ArrayList<>(((Artist) object).songSet));
        } else if (object instanceof Playlist) {
            return Observable.just(new ArrayList<>(((Playlist) object).songSet));
        } else {
            return null;
        }
    }

    private List<Song> shuffleQueueIfNeeded(List<Song> songs) {
        if (musicStateManager.observeMusicState().getValue().isShuffle()) {
            Collections.shuffle(songs);
        }
        return songs;
    }

    private void playCurrentSongIndex(Song song) {
        musicService.play(song.data).subscribe();
    }

}
