package com.gaurav.data.models;

public class SongEntity {
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

    public SongEntity() {
    }

    public SongEntity(long songId, long albumId, long artistId, String data, String title,
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
}
