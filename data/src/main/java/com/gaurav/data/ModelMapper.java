package com.gaurav.data;

import com.gaurav.data.models.SongEntity;
import com.gaurav.domain.models.Song;

public class ModelMapper {

    public Song convertSongEntityToSong(SongEntity songEntity) {
        return new Song(songEntity.songId, songEntity.albumId, songEntity.artistId,
                songEntity.data, songEntity.title, songEntity.duration, songEntity.album,
                songEntity.artist, songEntity.year, songEntity.track);
    }
}
