package com.gaurav.sangeet.viewmodels.songs;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.gaurav.domain.interfaces.MusicStateManager;
import com.gaurav.domain.models.Song;
import com.gaurav.domain.usecases.actions.PlaySongAction;
import com.gaurav.domain.usecases.interfaces.CommandUseCases;
import com.gaurav.domain.usecases.interfaces.FetchUseCases;
import com.gaurav.sangeet.MutableConstants;
import com.gaurav.sangeet.di.Injector;
import com.gaurav.sangeet.viewmodels.BaseViewModel;
import com.gaurav.sangeet.views.interfaces.SongsView;
import com.gaurav.sangeet.views.uievents.songs.SongItemClickUIEvent;
import com.gaurav.sangeet.views.viewstates.SongsViewState;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class SongsViewModel extends BaseViewModel {
    @Inject
    FetchUseCases fetchUseCases;
    @Inject
    CommandUseCases commandUseCases;
    @Inject
    MusicStateManager musicStateManager;

    private SongsView songsView = null;
    private MutableLiveData<SongsViewState> state;
    private MutableLiveData<Song> currentSongPlaying;

    @SuppressLint("CheckResult")
    public SongsViewModel() {
        Injector.get().inject(this);
        state = new MutableLiveData<>();
        currentSongPlaying = new MutableLiveData<>();
        state.setValue(new SongsViewState.Result(new ArrayList<>()));
        currentSongPlaying.setValue(null);

        compositeDisposable.add(
                fetchUseCases.getAllSongs()
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnSubscribe(__ -> state.setValue(new SongsViewState.Loading()))
                        .subscribe(songs -> state.setValue(new SongsViewState.Result(songs)),
                                throwable -> state.setValue(new SongsViewState.Error())));

        musicStateManager.observeMusicState()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(musicState -> {
                    if (currentSongPlaying.getValue() == null ||
                            !currentSongPlaying.getValue().equals(musicState.getCurrentSong())) {
                        currentSongPlaying.setValue(musicState.getCurrentSong());
                    }
                }, Throwable::printStackTrace);
    }

    public void attachSongsView(SongsView songsView) {
        this.songsView = songsView;
        bindIntents();
    }

    @Override
    protected void bindIntents() {
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

    public MutableLiveData<Song> getCurrentSongPlaying() {
        return currentSongPlaying;
    }

    public boolean isShouldAnimateList() {
        boolean temp = MutableConstants.SONG_VIEW_ANIMATE_SONG_ITEM;
        MutableConstants.SONG_VIEW_ANIMATE_SONG_ITEM = false;
        return temp;
    }
}
