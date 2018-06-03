package com.gaurav.sangeet.views.implementations.songs;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewModels.song.SongViewModel;
import com.gaurav.sangeet.viewModels.song.SongViewModelFactory;
import com.gaurav.sangeet.views.interfaces.SongView;

import java.util.ArrayList;

import io.reactivex.Emitter;
import io.reactivex.Observable;

@SuppressLint("ValidFragment")
public class SongViewImpl extends Fragment implements SongView {
    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;

    SongViewModel viewModel;
    RecyclerView recyclerView;
    SongRVAdapter songRVAdapter;
    private Emitter<Song> playSongEmitter;

    public SongViewImpl(FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs_view, container, false);
        songRVAdapter = new SongRVAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(songRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            playSongEmitter.onNext(songRVAdapter.getSong(position));
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this,
                new SongViewModelFactory(fetchUseCases, commandUseCases, this))
                .get(SongViewModel.class);
        viewModel.getSongViewState().observe(this, this::render);
    }

    @Override
    public void render(SongViewState songViewState) {
        if (songViewState instanceof SongViewState.Loading) {
            // show loading
        } else if (songViewState instanceof SongViewState.Error) {
            // show error
        } else {
            songRVAdapter.updateData(((SongViewState.Result) songViewState).getSongList());
        }
    }

    @Override
    public Observable<Song> playIntent() {
        return Observable.create(emitter -> this.playSongEmitter = emitter);
    }
}

