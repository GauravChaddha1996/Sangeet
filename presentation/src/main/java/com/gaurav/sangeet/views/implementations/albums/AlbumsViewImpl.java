package com.gaurav.sangeet.views.implementations.albums;

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

import com.gaurav.domain.models.Album;
import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewModels.albums.AlbumsViewModel;
import com.gaurav.sangeet.viewModels.albums.AlbumsViewModelFactory;
import com.gaurav.sangeet.views.interfaces.AlbumsView;

import java.util.ArrayList;

import io.reactivex.Emitter;
import io.reactivex.Observable;

@SuppressLint("ValidFragment")
public class AlbumsViewImpl extends Fragment implements AlbumsView {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;

    AlbumsViewModel viewModel;
    RecyclerView recyclerView;
    AlbumsRVAdapter albumsRVAdapter;
    Emitter<Album> playAlbumEmitter;

    public AlbumsViewImpl(FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.albums_view, container, false);
        albumsRVAdapter = new AlbumsRVAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(albumsRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            playAlbumEmitter.onNext(albumsRVAdapter.getAlbum(position));
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this,
                new AlbumsViewModelFactory(fetchUseCases, commandUseCases, this))
                .get(AlbumsViewModel.class);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void render(AlbumsViewState state) {
        if (state instanceof AlbumsViewState.Loading) {
            // show loading
        } else if (state instanceof AlbumsViewState.Error) {
            // show error
        } else {
            albumsRVAdapter.updateData(((AlbumsViewState.Result) state).getAlbumList());
        }
    }

    @Override
    public Observable<Album> playIntent() {
        return Observable.create(emitter -> playAlbumEmitter = emitter);
    }
}
