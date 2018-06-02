package com.gaurav.sangeet.views.songs;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaurav.sangeet.HomeViewModel;
import com.gaurav.sangeet.R;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;

@SuppressLint("ValidFragment")
public class SongView extends Fragment {
    HomeViewModel viewModel;
    RecyclerView recyclerView;
    SongRVAdapter songRVAdapter;

    public SongView(HomeViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs_view, container, false);
        songRVAdapter = new SongRVAdapter(new ArrayList<>(), inflater, song -> viewModel.play(song));
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(songRVAdapter);
        return view;
    }

    @SuppressLint("CheckResult")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel.getAllSongs()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(songRVAdapter::updateData);
    }
}
