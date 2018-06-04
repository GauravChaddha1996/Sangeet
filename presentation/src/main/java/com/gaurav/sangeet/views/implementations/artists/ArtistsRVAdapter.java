package com.gaurav.sangeet.views.implementations.artists;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Artist;
import com.gaurav.sangeet.R;

import java.util.List;

public class ArtistsRVAdapter extends RecyclerView.Adapter<ArtistsRVAdapter.ArtistsViewHolder> {

    List<Artist> data;

    public ArtistsRVAdapter(List<Artist> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ArtistsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ArtistsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistsViewHolder holder, int position) {
        holder.text.setText(data.get(position).name);
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
        TextView text;

        public ArtistsViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.rv_item_text);
        }
    }
}
