package com.gaurav.sangeet.views.implementations.albums;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.R;
import com.gaurav.sangeet.activity.AlbumDetailActivity;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewModels.albums.AlbumsViewModel;
import com.gaurav.sangeet.viewModels.albums.AlbumsViewModelFactory;
import com.gaurav.sangeet.views.interfaces.AlbumsView;
import com.gaurav.sangeet.views.uiEvents.albums.AlbumViewUIEvent;
import com.gaurav.sangeet.views.viewStates.AlbumsViewState;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

@SuppressLint("ValidFragment")
public class AlbumsViewImpl extends Fragment implements AlbumsView {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;

    AlbumsViewModel viewModel;
    RecyclerView recyclerView;
    AlbumsRVAdapter albumsRVAdapter;
    PublishSubject<AlbumViewUIEvent> uiEventsSubject;

    public AlbumsViewImpl(FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        uiEventsSubject = PublishSubject.create();
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
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) ->
        {
            // TODO: 7/15/18 Do this via proper navigation channels
//            uiEventsSubject.onNext(new AlbumItemClickUIEvent(albumsRVAdapter.getAlbum(position)));
            startActivity(new Intent(inflater.getContext(), AlbumDetailActivity.class).putExtra(
                    "albumId", albumsRVAdapter.getAlbum(position).id));
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
    public PublishSubject<AlbumViewUIEvent> getUIEvents() {
        return uiEventsSubject;
    }
}
