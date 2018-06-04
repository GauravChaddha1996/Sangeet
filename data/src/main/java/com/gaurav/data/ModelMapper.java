package com.gaurav.data;

import com.gaurav.data.models.PlaylistEntity;
import com.gaurav.domain.models.Playlist;

public class ModelMapper {

    public Playlist convertPlaylistEntityToPlaylist(PlaylistEntity playlistEntity) {
        return new Playlist(playlistEntity.id, playlistEntity.name, playlistEntity.songs);
    }

    public PlaylistEntity convertPlaylistToPlaylistEntity(Playlist playlist) {
        return new PlaylistEntity(playlist.id, playlist.name, playlist.songSet);
    }
}
