package com.gaurav.data;

import com.gaurav.data.models.AlbumEntity;
import com.gaurav.data.models.ArtistEntity;
import com.gaurav.data.models.SongEntity;
import com.gaurav.domain.models.Album;
import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;

import java.util.TreeSet;
import java.util.stream.Collectors;

public class ModelMapper {

    public Song convertSongEntityToSong(SongEntity songEntity) {
        return new Song(songEntity.songId, songEntity.albumId, songEntity.artistId,
                songEntity.data, songEntity.title, songEntity.duration, songEntity.album,
                songEntity.artist, songEntity.year, songEntity.track);
    }

    public Album convertAlbumEntityToAlbum(AlbumEntity albumEntity) {
        return new Album(albumEntity.id, albumEntity.name, albumEntity.artistId,
                albumEntity.artistName,
                albumEntity.songSet.stream()
                        .map(this::convertSongEntityToSong)
                        .collect(Collectors.toCollection(TreeSet::new)));
    }

    public Artist convertArtistEntityToArtist(ArtistEntity artistEntity) {
        return new Artist(artistEntity.id, artistEntity.name,
                artistEntity.albumSet.stream()
                        .map(this::convertAlbumEntityToAlbum)
                        .collect(Collectors.toCollection(TreeSet::new)),
                artistEntity.songSet.stream()
                        .map(this::convertSongEntityToSong)
                        .collect(Collectors.toCollection(TreeSet::new))
        );
    }
}
