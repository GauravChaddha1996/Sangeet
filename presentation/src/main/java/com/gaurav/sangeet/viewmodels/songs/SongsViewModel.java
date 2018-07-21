package com.gaurav.sangeet.viewmodels.songs;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.actions.PlaySongAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewmodels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.SongsView;
import com.gaurav.sangeet.views.uievents.songs.SongItemClickUIEvent;
import com.gaurav.sangeet.views.viewstates.SongsViewState;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SongsViewModel extends BaseViewModel {
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    private SongsView songsView;
    private MutableLiveData<SongsViewState> state;

    public SongsViewModel(SongsView songsView) {
        Injector.get().inject(this);
        this.songsView = songsView;

        bindIntents();

        state = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getAllSongs()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new SongsViewState.Loading()))
                        .subscribe(songs -> state.setValue(new SongsViewState.Result(songs)),
                                throwable -> state.setValue(new SongsViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(
                songsView.getUIEvents()
                        .map(songViewUIEvent -> {
                            // TODO: 7/7/18 think of a better way to do this if-else case
                            if (songViewUIEvent instanceof SongItemClickUIEvent) {
                                return new PlaySongAction(
                                        ((SongItemClickUIEvent) songViewUIEvent).getSong());
                            }
                            return new PlaySongAction(null);
                        })
                        .subscribe(action -> commandUseCases.actionSubject()
                                .onNext(action)));
    }

    public MutableLiveData<SongsViewState> getState() {
        return state;
    }

}
