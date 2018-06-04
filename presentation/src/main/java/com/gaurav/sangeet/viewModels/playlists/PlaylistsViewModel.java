package com.gaurav.sangeet.viewModels.playlists;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.implementations.playlists.PlaylistsViewState;
import com.gaurav.sangeet.views.interfaces.PlaylistsView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class PlaylistsViewModel extends BaseViewModel {
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;
    private PlaylistsView playlistsView;
    private MutableLiveData<PlaylistsViewState> state;

    public PlaylistsViewModel(FetchUseCases fetchUseCases, CommandUseCases commandUseCases, PlaylistsView playlistsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.playlistsView = playlistsView;

        bindIntents();

        state = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getAllPlaylists()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new PlaylistsViewState.Loading()))
                        .subscribe(playlists -> state.setValue(new PlaylistsViewState.Result(playlists)),
                                throwable -> state.setValue(new PlaylistsViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(playlistsView.playIntent().subscribe(playlist -> commandUseCases.play(playlist,-1)));
    }

    public MutableLiveData<PlaylistsViewState> getState() {
        return state;
    }

}
