package com.gaurav.sangeet.views.implementations.albums;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gaurav.domain.models.Album;
import com.gaurav.sangeet.R;

import java.util.List;

public class AlbumsRVAdapter extends RecyclerView.Adapter<AlbumsRVAdapter.AlbumsViewHolder> {

    List<Album> data;

    public AlbumsRVAdapter(List<Album> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public AlbumsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlbumsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumsViewHolder holder, int position) {
        holder.text.setText(data.get(position).name);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    void updateData(List<Album> albums) {
        this.data = albums;
        notifyDataSetChanged();
    }

    Album getAlbum(int pos) {
        return data.get(pos);
    }

    public class AlbumsViewHolder extends RecyclerView.ViewHolder {
        TextView text;

        public AlbumsViewHolder(View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.rv_item_text);
        }
    }
}
