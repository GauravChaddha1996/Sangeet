package com.gaurav.domain;

import com.gaurav.domain.interfaces.MusicInteractor;
import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MusicInteractorImpl implements MusicInteractor {
    private MusicRepository musicRepository;

    public MusicInteractorImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Single<List<Song>> getAllSongs() {
        return musicRepository.getAllSongs();
    }
}
