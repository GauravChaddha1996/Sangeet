package com.gaurav.sangeet.views.implementations.artists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaurav.domain.models.Artist;
import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ArtistsRVAdapter extends RecyclerView.Adapter<ArtistsRVAdapter.ArtistsViewHolder> {

    private List<Artist> data;
    private Artist artist;
    private Song song;
    private String albumsString, songsString;

    private RoundedCornersTransformation roundedCornersTransformation;

    public ArtistsRVAdapter(List<Artist> data) {
        this.data = data;
        roundedCornersTransformation = new RoundedCornersTransformation(16, 8);
    }

    @NonNull
    @Override
    public ArtistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.artist_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsViewHolder holder, int position) {
        artist = data.get(position);
        song = !artist.albumSet.isEmpty() ? artist.albumSet.first().songSet.first() : artist.songSet.first();
        if (!song.artworkPath.equals("null")) {
            Picasso.get().load(new File(song.artworkPath))
                    .transform(roundedCornersTransformation)
                    .centerCrop()
                    .resize(128, 128)
                    .into(holder.artistImage);
        } else {
            Picasso.get().load(R.drawable.default_album_item_icon)
                    .transform(roundedCornersTransformation)
                    .centerCrop()
                    .resize(128, 128)
                    .into(holder.artistImage);
        }
        holder.artistName.setText(artist.name);
        if (artist.albumSet.size() == 0) {
            albumsString = "";
        } else {
            albumsString = artist.albumSet.size() + " ";
            albumsString += artist.albumSet.size() == 1 ? "Album" : "Albums";
            albumsString += " •";
        }
        songsString = artist.songSet.size() == 1 ? "Song" : "Songs";
        holder.artistTotalAlbumsSongs.setText(String.format("%s %s %s", albumsString,
                artist.songSet.size(), songsString));
        holder.artistName.setSelected(true);
        holder.artistTotalAlbumsSongs.setSelected(true);
    }

    @Override
    public void onViewRecycled(@NonNull ArtistsViewHolder holder) {
        holder.artistName.setSelected(false);
        holder.artistTotalAlbumsSongs.setSelected(false);
        holder.artistImage.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void updateData(List<Artist> artists) {
        this.data = artists;
        notifyDataSetChanged();
    }

    Artist getArtist(int pos) {
        return data.get(pos);
    }

    public class ArtistsViewHolder extends RecyclerView.ViewHolder {
        TextView artistName;
        TextView artistTotalAlbumsSongs;
        ImageView artistImage;

        public ArtistsViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artistName);
            artistTotalAlbumsSongs = itemView.findViewById(R.id.artistTotalAlbumsSongs);
            artistImage = itemView.findViewById(R.id.artistItemImage);
        }
    }
}
