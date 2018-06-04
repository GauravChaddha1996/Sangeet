package com.gaurav.sangeet.viewModels.songs;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.implementations.songs.SongsViewState;
import com.gaurav.sangeet.views.interfaces.SongsView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SongsViewModel extends BaseViewModel {
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;
    private SongsView songsView;
    private MutableLiveData<SongsViewState> state;

    public SongsViewModel(FetchUseCases fetchUseCases, CommandUseCases commandUseCases, SongsView songsView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
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
        compositeDisposable.add(songsView.playIntent().subscribe(commandUseCases::play));
    }

    public MutableLiveData<SongsViewState> getState() {
        return state;
    }

}
