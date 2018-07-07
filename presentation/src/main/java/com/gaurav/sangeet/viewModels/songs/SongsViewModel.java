package com.gaurav.sangeet.viewModels.songs;

import android.arch.lifecycle.MutableLiveData;

import com.gaurav.domain.usecases.CommandUseCases;
import com.gaurav.domain.usecases.FetchUseCases;
import com.gaurav.domain.usecases.actions.PlaySongAction;
import com.gaurav.sangeet.viewModels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.SongsView;
import com.gaurav.sangeet.views.uiEvents.songs.SongItemClickUIEvent;
import com.gaurav.sangeet.views.viewStates.SongsViewState;

import io.reactivex.android.schedulers.AndroidSchedulers;

import static com.gaurav.domain.Utils.caseConstruct;
import static com.gaurav.domain.Utils.switchConstruct;

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
        compositeDisposable.add(
                songsView.getUIEvents()
                        .map(songViewUIEvent -> {
                            // TODO: 7/7/18 think of a better way to do this if-else case
                            if(songViewUIEvent instanceof SongItemClickUIEvent) {
                                return new PlaySongAction(((SongItemClickUIEvent) songViewUIEvent).getSong());
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
