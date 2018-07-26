package com.gaurav.domain.models;

import java.util.Objects;

public class Song implements Comparable {
    public long songId;
    public long albumId;
    public long artistId;
    public String data;
    public String title;
    public long duration;
    public String album;
    public String artist;
    public long year;
    public long track;
    public String artworkPath;

    public Song() {

    }

    public Song(long songId, long albumId, long artistId, String data, String title, long duration,
                String album, String artist, long year, long track, String artworkPath) {
        this.songId = songId;
        this.albumId = albumId;
        this.artistId = artistId;
        this.data = data;
        this.title = title;
        this.duration = duration;
        this.album = album;
        this.artist = artist;
        this.year = year;
        this.track = track;
        this.artworkPath = artworkPath;
    }

    @Override
    public int compareTo(Object o) {
        if (songId == ((Song) o).songId) {
            return 0;
        }
        return songId < ((Song) o).songId ? -1 : 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Song)) return false;
        Song song = (Song) o;
        return songId == song.songId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(songId);
    }
}
