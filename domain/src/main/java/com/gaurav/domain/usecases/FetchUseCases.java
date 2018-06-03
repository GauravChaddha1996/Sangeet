package com.gaurav.domain.usecases;

import com.gaurav.domain.models.Song;

import java.util.List;

import io.reactivex.Observable;

public interface FetchUseCases {
    Observable<List<Song>> getAllSongs();
}
