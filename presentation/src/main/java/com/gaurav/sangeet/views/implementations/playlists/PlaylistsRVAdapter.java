package com.gaurav.sangeet.views.implementations.playlists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Playlist;
import com.gaurav.sangeet.R;

import java.util.List;

public class PlaylistsRVAdapter extends RecyclerView.Adapter<PlaylistsRVAdapter.PlaylistsViewHolder> {

    List<Playlist> data;

    public PlaylistsRVAdapter(List<Playlist> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public PlaylistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PlaylistsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistsViewHolder holder, int position) {
        holder.text.setText(data.get(position).name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void updateData(List<Playlist> playlists) {
        this.data = playlists;
        notifyDataSetChanged();
    }

    Playlist getPlaylist(int pos) {
        return data.get(pos);
    }

    public class PlaylistsViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public PlaylistsViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.rv_item_text);
        }
    }
}
