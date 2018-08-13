package com.gaurav.sangeet.viewmodels.albums;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.actions.PlayAlbumAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewmodels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.AlbumsView;
import com.gaurav.sangeet.views.uievents.albums.AlbumItemClickUIEvent;
import com.gaurav.sangeet.views.viewstates.AlbumsViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class AlbumsViewModel extends BaseViewModel {
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    private AlbumsView albumsView;
    private MutableLiveData<AlbumsViewState> state;

    public AlbumsViewModel() {
        Injector.get().inject(this);

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
                        return new PlayAlbumAction(((AlbumItemClickUIEvent)
                                albumViewUIEvent).getAlbum(), ((AlbumItemClickUIEvent)
                                albumViewUIEvent).getAlbum().songSet.first());
                    }
                    return new PlayAlbumAction(null, null);
                }).subscribe(playAlbumAction -> commandUseCases.actionSubject()
                        .onNext(playAlbumAction)));
    }

    public void attachAlbumsView(AlbumsView albumsView) {
        this.albumsView = albumsView;
        bindIntents();
    }

    public MutableLiveData<AlbumsViewState> getState() {
        return state;
    }
}
