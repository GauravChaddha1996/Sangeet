package com.gaurav.sangeet.views.implementations.albums;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gaurav.domain.models.Album;
import com.gaurav.sangeet.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.List;

import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class AlbumsRVAdapter extends RecyclerView.Adapter<AlbumsRVAdapter.AlbumsViewHolder> {

    private List<Album> data;
    private Album album;
    private Transformation roundedCornersTransformation;

    public AlbumsRVAdapter(List<Album> data) {
        this.data = data;
        roundedCornersTransformation = new RoundedCornersTransformation(12, 8);
    }

    @NonNull
    @Override
    public AlbumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsViewHolder holder, int position) {
        album = data.get(position);
        if (!album.songSet.first().artworkPath.equals("null")) {
            Picasso.get().load(new File(album.songSet.first().artworkPath))
                    .transform(roundedCornersTransformation)
                    .centerCrop()
                    .resize(256, 256)
                    .into(holder.backgroundImage);
        } else {
            Picasso.get().load(R.drawable.default_album_item_icon)
                    .transform(roundedCornersTransformation)
                    .centerCrop()
                    .resize(256, 256)
                    .into(holder.backgroundImage);
        }
        holder.albumTitle.setText(album.name);
        holder.artistTotalSongs.setText(String.format("%s • %s %s",
                album.multipleArtists ? "Various Artists" : album.artistName,
                album.songSet.size(), album.songSet.size() == 1 ? "Song" : "Songs"));
    }

    @Override
    public void onViewRecycled(@NonNull AlbumsViewHolder holder) {
        holder.backgroundImage.setImageDrawable(null);
        super.onViewRecycled(holder);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Album> albums) {
        this.data = albums;
        notifyDataSetChanged();
    }

    public Album getAlbum(int pos) {
        return data.get(pos);
    }

    public class AlbumsViewHolder extends RecyclerView.ViewHolder {
        TextView albumTitle;
        TextView artistTotalSongs;
        ImageView backgroundImage;

        public AlbumsViewHolder(View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.albumTitle);
            artistTotalSongs = itemView.findViewById(R.id.artistTotalSongs);
            backgroundImage = itemView.findViewById(R.id.albumIcon);
        }
    }
}
