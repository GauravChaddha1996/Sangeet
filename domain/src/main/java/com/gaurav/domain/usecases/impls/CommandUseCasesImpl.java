package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.interfaces.MusicInteractor;
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
}
