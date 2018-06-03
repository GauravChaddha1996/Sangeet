package com.gaurav.domain.usecases.impls;

import com.gaurav.domain.interfaces.MusicRepository;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.FetchUseCases;

import java.util.List;

import io.reactivex.Observable;

public class FetchUseCasesImpl implements FetchUseCases {
    MusicRepository musicRepository;

    public FetchUseCasesImpl(MusicRepository musicRepository) {
        this.musicRepository = musicRepository;
    }

    @Override
    public Observable<List<Song>> getAllSongs() {
        return musicRepository.getAllSongs();
    }
}
