package com.gaurav.sangeet.views.implementations.playlists;

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

import com.gaurav.domain.models.Playlist;
import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewModels.playlists.PlaylistsViewModel;
import com.gaurav.sangeet.viewModels.playlists.PlaylistsViewModelFactory;
import com.gaurav.sangeet.views.interfaces.PlaylistsView;

import java.util.ArrayList;

import io.reactivex.Emitter;
import io.reactivex.Observable;

@SuppressLint("ValidFragment")
public class PlaylistsViewImpl extends Fragment implements PlaylistsView {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;

    PlaylistsViewModel viewModel;
    RecyclerView recyclerView;
    PlaylistsRVAdapter playlistRVAdapter;
    Emitter<Playlist> playPlaylistEmitter;

    public PlaylistsViewImpl(FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlists_view, container, false);
        playlistRVAdapter = new PlaylistsRVAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(playlistRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            playPlaylistEmitter.onNext(playlistRVAdapter.getPlaylist(position));
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this,
                new PlaylistsViewModelFactory(fetchUseCases, commandUseCases, this))
                .get(PlaylistsViewModel.class);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void render(PlaylistsViewState state) {
        if (state instanceof PlaylistsViewState.Loading) {
            // show loading
        } else if (state instanceof PlaylistsViewState.Error) {
            // show error
        } else {
            playlistRVAdapter.updateData(((PlaylistsViewState.Result) state).getPlaylistList());
        }
    }

    @Override
    public Observable<Playlist> playIntent() {
        return Observable.create(emitter -> playPlaylistEmitter = emitter);
    }
}
