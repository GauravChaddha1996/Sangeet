package com.gaurav.sangeet.views.songs;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;

import java.util.List;

public class SongRVAdapter extends RecyclerView.Adapter<SongRVAdapter.SongItemViewHolder> {

    private List<Song> data;
    private LayoutInflater layoutInflater;
    private OnClickListener listener;

    public SongRVAdapter(List<Song> data, LayoutInflater layoutInflater, OnClickListener listener) {
        this.data = data;
        this.layoutInflater = layoutInflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SongItemViewHolder(layoutInflater.inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SongItemViewHolder holder, int position) {
        holder.text.setText(data.get(position).title);
        holder.view.setOnClickListener((View v) -> listener.onClick(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(List<Song> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    class SongItemViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        View view;

        SongItemViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            text = itemView.findViewById(R.id.rv_item_text);
        }
    }

    public interface OnClickListener {
        void onClick(Song song);
    }
}
