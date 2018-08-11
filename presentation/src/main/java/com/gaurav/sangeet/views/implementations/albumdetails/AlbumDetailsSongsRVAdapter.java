package com.gaurav.sangeet.views.implementations.albumdetails;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.Utils;

import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AlbumDetailsSongsRVAdapter extends RecyclerView.Adapter<AlbumDetailsSongsRVAdapter.SongItemViewHolder> {

    private List<Song> data;
    private Integer currentPlayingSongIndex;

    // helper private variables
    private Song song;
    private int accentColor;
    private int defaultTitleColor;
    private RoundedCornersTransformation roundedCornerTransformation;


    public AlbumDetailsSongsRVAdapter(List<Song> data) {
        this.data = data;
        this.currentPlayingSongIndex = -1;
        roundedCornerTransformation = new RoundedCornersTransformation(16, 16);
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        accentColor = parent.getContext().getResources().getColor(R.color.colorAccent);
        defaultTitleColor = parent.getContext().getResources().getColor(R.color.songTitleTextColor);
        return new SongItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_detail_song_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        song = data.get(position);
        holder.songTrackNumber.setText(String.valueOf(song.track));
        holder.songTitle.setText(song.title);
        holder.songArtistAlbum.setText(String.format("%s • %s", song.artist, song.album));
        holder.songDuration.setText(Utils.convertLongToDuration(song.duration));
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).songId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Song> newData) {
        this.data.clear();
        this.data.addAll(newData);
        notifyDataSetChanged();
    }

    public Song getSong(int pos) {
        return data.get(pos);
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        TextView songTrackNumber;
        TextView songTitle;
        TextView songArtistAlbum;
        TextView songDuration;

        SongItemViewHolder(View itemView) {
            super(itemView);
            songTrackNumber = itemView.findViewById(R.id.songTrackNumber);
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtistAlbum = itemView.findViewById(R.id.songArtistAlbum);
            songDuration = itemView.findViewById(R.id.songDuration);
        }
    }
}
