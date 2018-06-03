package com.gaurav.domain.usecases;

import com.gaurav.domain.models.Song;

public interface CommandUseCases {
    void play(Song song);
}
