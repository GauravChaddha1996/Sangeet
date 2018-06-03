package com.gaurav.sangeet.viewModels.song;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.implementations.songs.SongViewState;
import com.gaurav.sangeet.views.interfaces.SongView;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SongViewModel extends BaseViewModel {
    private FetchUseCases fetchUseCases;
    private CommandUseCases commandUseCases;
    private SongView songView;
    private MutableLiveData<SongViewState> songViewState;

    public SongViewModel(FetchUseCases fetchUseCases, CommandUseCases commandUseCases, SongView songView) {
        this.fetchUseCases = fetchUseCases;
        this.commandUseCases = commandUseCases;
        this.songView = songView;

        bindIntents();

        songViewState = new MutableLiveData<>();
        compositeDisposable.add(
                fetchUseCases.getAllSongs()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> songViewState.setValue(new SongViewState.Loading()))
                        .subscribe(songs -> songViewState.setValue(new SongViewState.Result(songs)),
                                throwable -> songViewState.setValue(new SongViewState.Error())));
    }

    @Override
    public void bindIntents() {
        compositeDisposable.add(songView.playIntent().subscribe(commandUseCases::play));
    }

    public MutableLiveData<SongViewState> getSongViewState() {
        return songViewState;
    }

}
