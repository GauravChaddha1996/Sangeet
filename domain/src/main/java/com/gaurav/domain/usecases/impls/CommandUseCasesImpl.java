package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.CommandUseCases;

import io.reactivex.disposables.Disposable;

public class CommandUseCasesImpl implements CommandUseCases {

    MusicInteractor musicInteractor;

    public CommandUseCasesImpl(MusicInteractor musicInteractor) {
        this.musicInteractor = musicInteractor;
    }

    @Override
    public void play(Song song) {
        Disposable disposable = musicInteractor.play(song).subscribe(() -> {
        }, Throwable::printStackTrace);
    }

    @Override
    public void play(Album album) {
        Disposable disposable = musicInteractor.play(album, -1).subscribe(() -> {
        }, Throwable::printStackTrace);
    }

    @Override
    public void play(Artist artist) {
        Disposable disposable = musicInteractor.play(artist, -1).subscribe(() -> {
        }, Throwable::printStackTrace);
    }

    @Override
    public void play(Playlist playlist) {
        Disposable disposable = musicInteractor.play(playlist, -1).subscribe(() -> {
        }, Throwable::printStackTrace);

    }
}
