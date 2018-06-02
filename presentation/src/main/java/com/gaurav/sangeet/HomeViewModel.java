package com.gaurav.sangeet;

import android.arch.lifecycle.ViewModel;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Single;

public class HomeViewModel extends ViewModel {
    MusicInteractor musicInteractor;

    public HomeViewModel(MusicInteractor musicInteractor) {
        this.musicInteractor = musicInteractor;
    }

    public Single<List<Song>> getAllSongs() {
        return musicInteractor.getAllSongs();
    }

    public void play(Song song) {
        musicInteractor.play(song).subscribe();
    }

}
