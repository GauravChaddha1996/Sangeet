package com.gaurav.sangeet.views.implementations.songs;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.gaurav.sangeet.R;
import com.gaurav.sangeet.utils.ItemClickSupport;
import com.gaurav.sangeet.viewmodels.songs.SongsViewModel;
import com.gaurav.sangeet.viewmodels.songs.SongsViewModelFactory;
import com.gaurav.sangeet.views.interfaces.SongsView;
import com.gaurav.sangeet.views.uievents.songs.SongItemClickUIEvent;
import com.gaurav.sangeet.views.uievents.songs.SongViewUIEvent;
import com.gaurav.sangeet.views.viewstates.SongsViewState;

import java.util.ArrayList;

import io.reactivex.subjects.PublishSubject;

import static android.support.v7.widget.DividerItemDecoration.VERTICAL;

@SuppressLint("ValidFragment")
public class SongsViewImpl extends Fragment implements SongsView {
    SongsViewModel viewModel;
    RecyclerView recyclerView;
    SongsRVAdapter songsRVAdapter;
    private PublishSubject<SongViewUIEvent> uiEventsSubject;

    public SongsViewImpl() {
        this.uiEventsSubject = PublishSubject.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.songs_view, container, false);
        songsRVAdapter = new SongsRVAdapter(new ArrayList<>());
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), VERTICAL));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(songsRVAdapter);
        ItemClickSupport.addTo(recyclerView).setOnItemClickListener((recyclerView, position, v) -> {
            uiEventsSubject.onNext(new SongItemClickUIEvent(songsRVAdapter.getSong(position)));
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(this,
                new SongsViewModelFactory(this))
                .get(SongsViewModel.class);
        viewModel.getState().observe(this, this::render);
    }

    @Override
    public void render(SongsViewState songsViewState) {
        if (songsViewState instanceof SongsViewState.Loading) {
            // show loading
        } else if (songsViewState instanceof SongsViewState.Error) {
            // show error
        } else {
            if (((SongsViewState.Result) songsViewState).isCurrentSongIndexChanged()) {
                songsRVAdapter.updateCurrentSongPlayingIndex(
                        ((SongsViewState.Result) songsViewState).getCurrentPlayingSong()
                );
            } else {
                songsRVAdapter.updateData(((SongsViewState.Result) songsViewState).getSongList());
                if (viewModel.isShouldAnimateList()) {
                    recyclerView.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(
                            this.getContext(), R.anim.song_list_layout_animation));
                    recyclerView.scheduleLayoutAnimation();
                }
            }

        }
    }

    @Override
    public PublishSubject<SongViewUIEvent> getUIEvents() {
        return uiEventsSubject;
    }
}

