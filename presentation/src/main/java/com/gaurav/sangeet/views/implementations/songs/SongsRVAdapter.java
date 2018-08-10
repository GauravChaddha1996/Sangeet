package com.gaurav.sangeet.views.implementations.songs;

import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.Utils;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class SongsRVAdapter extends RecyclerView.Adapter<SongsRVAdapter.SongItemViewHolder> {

    private List<Song> data;
    private Integer currentPlayingSongIndex;

    // helper private variables
    private Song song;
    private int accentColor;
    private int defaultTitleColor;
    private RoundedCornersTransformation roundedCornerTransformation;


    public SongsRVAdapter(List<Song> data) {
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
                .inflate(R.layout.song_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        song = data.get(position);
        if (position == currentPlayingSongIndex) {
            Picasso.get().load(R.drawable.song_item_currently_playing)
                    .transform(roundedCornerTransformation)
                    .centerCrop()
                    .resize(256, 256)
                    .into(holder.songIcon);
        } else {
            if (!song.artworkPath.equals("null")) {
                Picasso.get().load(new File(song.artworkPath))
                        .transform(roundedCornerTransformation)
                        .centerCrop()
                        .resize(256, 256)
                        .into(holder.songIcon);
            } else {
                Picasso.get().load(R.drawable.default_song_item_icon)
                        .transform(roundedCornerTransformation)
                        .centerCrop()
                        .resize(256, 256)
                        .into(holder.songIcon);
            }
        }
        updateTitleColor(holder, position);
        holder.songTitle.setText(song.title);
        holder.songArtistAlbum.setText(String.format("%s • %s", song.artist, song.album));
        holder.songDuration.setText(Utils.convertLongToDuration(song.duration));
    }

    @Override
    public void onViewRecycled(@NonNull SongItemViewHolder holder) {
        holder.songIcon.setImageDrawable(null);
        super.onViewRecycled(holder);
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

    public void updateCurrentSongPlayingIndex(Song newSong) {
        int newPos = data.indexOf(newSong);
        int oldPos = this.currentPlayingSongIndex;
        this.currentPlayingSongIndex = newPos;
        if (oldPos != -1) {
            notifyItemChanged(oldPos);
        }
        notifyItemChanged(newPos);

    }

    private void updateTitleColor(SongItemViewHolder viewHolder, int pos) {
        if (pos == currentPlayingSongIndex && !song.artworkPath.equals("null")) {
            new Palette.Builder(BitmapFactory.decodeFile(song.artworkPath))
                    .generate(palette ->
                            viewHolder.songTitle.setTextColor(palette.getDarkVibrantColor(accentColor)));
        } else if (pos == currentPlayingSongIndex) {
            viewHolder.songTitle.setTextColor(accentColor);
        } else {
            viewHolder.songTitle.setTextColor(defaultTitleColor);
        }
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        ImageView songIcon;
        TextView songTitle;
        TextView songArtistAlbum;
        TextView songDuration;

        SongItemViewHolder(View itemView) {
            super(itemView);
            songIcon = itemView.findViewById(R.id.songIcon);
            songTitle = itemView.findViewById(R.id.songTitle);
            songArtistAlbum = itemView.findViewById(R.id.songArtistAlbum);
            songDuration = itemView.findViewById(R.id.songDuration);
        }
    }
}
