package com.gaurav.sangeet.views.implementations.songs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;

import java.util.List;

public class SongsRVAdapter extends RecyclerView.Adapter<SongsRVAdapter.SongItemViewHolder> {

    List<Song> data;

    public SongsRVAdapter(List<Song> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongItemViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        holder.text.setText(data.get(position).title);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).songId;
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void updateData(List<Song> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    Song getSong(int pos) {
        return data.get(pos);
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        SongItemViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.rv_item_text);
        }
    }
}
