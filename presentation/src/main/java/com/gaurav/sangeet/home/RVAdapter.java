package com.gaurav.sangeet.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Song;
import com.gaurav.sangeet.R;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolder> {

    List<Song> data;
    LayoutInflater inflater;
    OnClickListener listener;

    public RVAdapter(List<Song> data, LayoutInflater inflater, OnClickListener listener) {
        this.data = data;
        this.inflater = inflater;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RVViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RVViewHolder(inflater.inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolder holder, int position) {
        holder.text.setText(data.get(position).title);
        holder.view.setOnClickListener((View v) -> {
            listener.onClick(data.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class RVViewHolder extends RecyclerView.ViewHolder {
        TextView text;
        View view;

        public RVViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            text = itemView.findViewById(R.id.rv_item_text);
        }
    }

    public interface OnClickListener{
        void onClick(Song song);
    }
}
