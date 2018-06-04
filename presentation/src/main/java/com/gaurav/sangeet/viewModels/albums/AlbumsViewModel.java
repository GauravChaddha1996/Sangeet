package com.gaurav.sangeet.viewModels.albums;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.implementations.albums.AlbumsViewState;
import com.gaurav.sangeet.views.interfaces.AlbumsView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlbumsViewModel extends BaseViewModel {
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;
    private AlbumsView albumsView;
    private MutableLiveData<AlbumsViewState> state;

    public AlbumsViewModel(FetchUseCases fetchUseCases, CommandUseCases commandUseCases, AlbumsView albumsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.albumsView = albumsView;

        bindIntents();

        state = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getAllAlbums()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new AlbumsViewState.Loading()))
                        .subscribe(albums -> state.setValue(new AlbumsViewState.Result(albums)),
                                throwable -> state.setValue(new AlbumsViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(albumsView.playIntent().subscribe(commandUseCases::play));
    }

    public MutableLiveData<AlbumsViewState> getState() {
        return state;
    }
}
