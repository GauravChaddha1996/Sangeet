package com.gaurav.sangeet.viewModels.albums;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.actions.PlayAlbumAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.MusicApplication;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.AlbumsView;
import com.gaurav.sangeet.views.uiEvents.albums.AlbumItemClickUIEvent;
import com.gaurav.sangeet.views.viewStates.AlbumsViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlbumsViewModel extends BaseViewModel {
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    private AlbumsView albumsView;
    private MutableLiveData<AlbumsViewState> state;

    public AlbumsViewModel(AlbumsView albumsView) {
        Injector.get().inject(this);
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
        compositeDisposable.add(albumsView.getUIEvents()
                .map(albumViewUIEvent -> {
                    if (albumViewUIEvent instanceof AlbumItemClickUIEvent) {
                        return new PlayAlbumAction(((AlbumItemClickUIEvent) albumViewUIEvent).getAlbum(),
                                ((AlbumItemClickUIEvent) albumViewUIEvent).getAlbum().songSet.first());
                    }
                    return new PlayAlbumAction(null, null);
                }).subscribe(playAlbumAction -> commandUseCases.actionSubject()
                        .onNext(playAlbumAction)));
    }

    public MutableLiveData<AlbumsViewState> getState() {
        return state;
    }
}
