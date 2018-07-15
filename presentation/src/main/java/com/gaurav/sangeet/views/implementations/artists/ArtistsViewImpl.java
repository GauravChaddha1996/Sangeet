package com.gaurav.sangeet.views.implementations.artists;

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
import com.gaurav.sangeet.activity.ArtistDetailActivity;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewModels.artists.ArtistsViewModel;
import com.gaurav.sangeet.viewModels.artists.ArtistsViewModelFactory;
import com.gaurav.sangeet.views.interfaces.ArtistsView;
import com.gaurav.sangeet.views.uiEvents.artists.ArtistsViewUIEvent;
import com.gaurav.sangeet.views.viewStates.ArtistsViewState;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

@SuppressLint("ValidFragment")
public class ArtistsViewImpl extends Fragment implements ArtistsView {

    FetchUseCases fetchUseCases;
    CommandUseCases commandUseCases;

    ArtistsViewModel viewModel;
    RecyclerView recyclerView;
    ArtistsRVAdapter artistsRVAdapter;
    PublishSubject<ArtistsViewUIEvent> uiEventsSubject;

    public ArtistsViewImpl(FetchUseCases fetchUseCases, CommandUseCases commandUseCases) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        uiEventsSubject = PublishSubject.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artists_view, container, false);
        artistsRVAdapter = new ArtistsRVAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(artistsRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
//            uiEventsSubject.onNext(new ArtistItemClickUIEvent(artistsRVAdapter.getArtist(position)));
            startActivity(new Intent(inflater.getContext(), ArtistDetailActivity.class).putExtra(
                    "artistId", artistsRVAdapter.getArtist(position).id));
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this,
                new ArtistsViewModelFactory(fetchUseCases, commandUseCases, this))
                .get(ArtistsViewModel.class);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void render(ArtistsViewState state) {
        if (state instanceof ArtistsViewState.Loading) {
            // show loading
        } else if (state instanceof ArtistsViewState.Error) {
            // show error
        } else {
            artistsRVAdapter.updateData(((ArtistsViewState.Result) state).getArtistList());
        }
    }

    @Override
    public PublishSubject<ArtistsViewUIEvent> getUIEvents() {
        return uiEventsSubject;
    }
}
