package com.gaurav.sangeet.views.implementations.artists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
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

    /* Helper variables*/
    private Artist artist;
    private Song song;
    private String albumsString, songsString;
    private RoundedCornersTransformation roundedCornersTransformation;

    public ArtistsRVAdapter(List<Artist> data) {
        this.data = data;
        roundedCornersTransformation = new RoundedCornersTransformation(12, 8);
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
        String artworkPath = "null";
        for (Song song : artist.songSet) {
            if (!song.artworkPath.equals("null")) {
                artworkPath = song.artworkPath;
                break;
            }
        }
        if (!artworkPath.equals("null")) {
            Picasso.get().load(new File(artworkPath))
                    .transform(roundedCornersTransformation)
                    .centerCrop()
                    .resize(256, 256)
                    .into(holder.artistIcon);
        } else {
            Picasso.get().load(R.drawable.default_item_icon)
                    .transform(roundedCornersTransformation)
                    .centerCrop()
                    .resize(256, 256)
                    .into(holder.artistIcon);
        }
        holder.artistTitle.setText(artist.name);
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
    }

    @Override
    public void onViewRecycled(@NonNull ArtistsViewHolder holder) {
        holder.artistIcon.setImageDrawable(null);
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
        TextView artistTitle;
        TextView artistTotalAlbumsSongs;
        ImageView artistIcon;

        public ArtistsViewHolder(View itemView) {
            super(itemView);
            artistTitle = itemView.findViewById(R.id.artistTitle);
            artistTotalAlbumsSongs = itemView.findViewById(R.id.artistTotalAlbumsSongs);
            artistIcon = itemView.findViewById(R.id.artistIcon);
        }
    }
}
