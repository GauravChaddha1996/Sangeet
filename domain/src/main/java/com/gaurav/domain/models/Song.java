package com.gaurav.domain.models;

public class Song {
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

    public Song(long songId, long albumId, long artistId, String data, String title,
                long duration, String album, String artist, long year, long track) {
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
    }

    @Override
    public String toString() {
        return "Song = [ " +
                "songId=" + songId +
                ", albumId=" + albumId +
                ", artistId=" + artistId +
                ", data='" + data +
                ", title='" + title +
                ", duration=" + duration +
                ", album='" + album +
                ", artist='" + artist +
                ", year=" + year +
                ", track=" + track +
                " ] \n";
    }
}
